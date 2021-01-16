package tupperdate.android.data.features.messages.impl

import androidx.work.WorkManager
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.SyncRequestBuilder
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.messages.*
import tupperdate.android.data.features.messages.room.PendingMessageEntity
import tupperdate.android.data.features.messages.store.*
import tupperdate.android.data.features.messages.work.RefreshMessagesWorker
import tupperdate.android.data.features.messages.work.SendPendingMessagesWorker
import tupperdate.android.data.room.TupperdateDatabase
import java.util.*

@InternalDataApi
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MessagesRepositoryImpl(
    auth: AuthenticationRepository,
    private val database: TupperdateDatabase,
    private val manager: WorkManager,
    client: HttpClient,
) : MessagesRepository {

    private val allConversationsStore = StoreBuilder.from(
        fetcher = AllConversationFetcher(client),
        sourceOfTruth = AllConversationSourceOfTruth(database.conversations()),
    ).build()

    private val singleConversationStore = StoreBuilder.from(
        fetcher = ConversationFetcher(client),
        sourceOfTruth = OneConversationSourceOfTruth(database.conversations()),
    ).build()

    private val singleConversationMessagesStore = StoreBuilder.from(
        fetcher = MessagesFetcher(client),
        sourceOfTruth = MessagesSourceOfTruth(auth, database.messages())
    ).build()

    /**
     * Returns a [Flow] of all the conversations that are currently available on the device. These
     * conversations will automatically get updated as sync processes take place.
     */
    private fun allConversations() = allConversationsStore
        .stream(StoreRequest.cached(Unit, true))
        .map { it.dataOrNull() }
        .filterNotNull()

    override val pending: Flow<List<PendingMatch>> = allConversations()
        .map {
            it.map { conv ->
                if (conv.accepted) null
                else {
                    val (theirs, mine) = database.conversations()
                        .conversationRecipeAllOnce(conv.identifier)
                        .partition { recipe -> recipe.recipeBelongsToThem }
                    PendingMatch(
                        identifier = conv.identifier,
                        myPicture = mine.asSequence()
                            .map { r -> r.recipePicture }
                            .filterNotNull()
                            .firstOrNull(),
                        theirPicture = theirs.asSequence()
                            .map { r -> r.recipePicture }
                            .filterNotNull()
                            .firstOrNull(),
                    )
                }
            }
        }
        .map { it.filterNotNull() }
        .flowOn(Dispatchers.Default)

    override val matches: Flow<List<Match>> = allConversations()
        .map {
            it.map { conv ->
                if (conv.accepted &&
                    conv.previewBody == null &&
                    conv.previewTimestamp == null
                ) {
                    val (theirs, mine) = database.conversations()
                        .conversationRecipeAllOnce(conv.identifier)
                        .partition { recipe -> recipe.recipeBelongsToThem }
                    Match(
                        identifier = conv.identifier,
                        myPictures = mine.map { r -> r.recipePicture },
                        theirPictures = theirs.map { r -> r.recipePicture },
                    )
                } else {
                    null
                }
            }
        }
        .map { it.filterNotNull() }
        .flowOn(Dispatchers.Default)


    override val conversations: Flow<List<Conversation>> = allConversations()
        .map {
            it.map { entity ->
                if (entity.previewTimestamp == null ||
                    entity.previewBody == null
                ) null
                else {
                    val (theirs, mine) = database.conversations()
                        .conversationRecipeAllOnce(entity.identifier)
                        .partition { recipe -> recipe.recipeBelongsToThem }
                    Conversation(
                        identifier = entity.identifier,
                        picture = entity.picture,
                        previewTitle = entity.name,
                        previewBody = entity.previewBody,
                        previewTimestamp = entity.previewTimestamp,
                        myRecipePictures = mine.map { r -> r.recipePicture },
                        theirRecipePictures = theirs.map { r -> r.recipePicture },
                    )
                }
            }
        }
        .map { it.filterNotNull() }
        .flowOn(Dispatchers.Default)

    override fun conversation(
        with: FirebaseUid,
    ): Flow<Conversation?> = singleConversationStore.stream(StoreRequest.cached(with, true))
        .map { it.dataOrNull() }
        .map {
            it?.let { entity ->
                if (entity.previewTimestamp == null ||
                    entity.previewBody == null
                ) null
                else {
                    val (theirs, mine) = database.conversations()
                        .conversationRecipeAllOnce(entity.identifier)
                        .partition { recipe -> recipe.recipeBelongsToThem }
                    Conversation(
                        identifier = entity.identifier,
                        picture = entity.picture,
                        previewTitle = entity.name,
                        previewBody = entity.previewBody,
                        previewTimestamp = entity.previewTimestamp,
                        myRecipePictures = mine.map { r -> r.recipePicture },
                        theirRecipePictures = theirs.map { r -> r.recipePicture },
                    )
                }
            }
        }

    override fun conversationInfo(
        with: FirebaseUid,
    ): Flow<ConversationInfo?> = singleConversationStore.stream(StoreRequest.cached(with, true))
        .map { it.dataOrNull() }
        .map {
            it?.let { entity ->
                ConversationInfo(entity.name, entity.picture)
            }
        }

    private fun singleConversationPendingMessages(uid: FirebaseUid) = database.messages()
        .pending(uid)

    override fun messages(
        other: FirebaseUid,
    ): Flow<List<Message>> {
        // Actually received messages.
        val received = singleConversationMessagesStore
            .stream(StoreRequest.cached(other, true))
            .map { it.dataOrNull() }
            .filterNotNull()
        // Pending messages, which are soon to be sent.
        val sent = singleConversationPendingMessages(other)
        return combine(received, sent) { r, s ->
            // Add all the "standard" server messages.
            val start = r.map { msg ->
                Message(
                    identifier = msg.identifier,
                    body = msg.body,
                    timestamp = msg.timestamp,
                    from = if (msg.from == other) Sender.Other else Sender.Myself,
                    pending = false,
                )
            }
            val maxTimestamp = start.maxOfOrNull { it.timestamp } ?: 0
            // Add our local, pending messages, but...
            val end = s
                // ... only if they're not the duplicate of a server message, and...
                .filter { item -> r.none { it.localIdentifier == item.identifier } }
                // ... make sure they're visually distinct.
                .map { msg ->
                    Message(
                        identifier = msg.identifier,
                        body = msg.body,
                        // Make sure our local messages are after server-issued messages. The
                        // timestamps for local messages are not displayed, so we can leverage them
                        // to get correct sorting.
                        timestamp = msg.timestamp + maxTimestamp,
                        from = Sender.Myself,
                        pending = true,
                    )
                }
            start + end
        }
    }

    override suspend fun send(
        to: FirebaseUid,
        message: String,
    ) {
        database.messages().post(
            PendingMessageEntity(
                identifier = UUID.randomUUID().toString(),
                recipient = to,
                body = message,
                timestamp = System.currentTimeMillis(),
                sent = false,
            )
        )

        manager
            .beginWith(SyncRequestBuilder<SendPendingMessagesWorker>().build())
            .then(
                SyncRequestBuilder<RefreshMessagesWorker>()
                    .setInputData(RefreshMessagesWorker.forConversation(to))
                    .build()
            )
            .enqueue()
    }

    override suspend fun accept(match: PendingMatch) {
        database.conversations().accept(match.identifier)
    }
}
