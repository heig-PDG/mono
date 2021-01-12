package tupperdate.android.data.features.messages.impl

import androidx.work.WorkManager
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.messages.*
import tupperdate.android.data.features.messages.store.AllConversationFetcher
import tupperdate.android.data.features.messages.store.AllConversationSourceOfTruth
import tupperdate.android.data.features.messages.store.ConversationFetcher
import tupperdate.android.data.features.messages.store.OneConversationSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

@InternalDataApi
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MessagesRepositoryImpl(
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
                // TODO : Fetch the actual recipes.
                else PendingMatch(
                    identifier = conv.identifier,
                    myPicture = null,
                    theirPicture = null,
                )
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
                    Match(
                        identifier = conv.identifier,
                        myPictures = emptyList(), // TODO : Fetch actual recipes.
                        theirPictures = emptyList(), // TODO : Fetch actual recipes.
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
                else Conversation(
                    identifier = entity.identifier,
                    picture = entity.picture,
                    previewTitle = entity.name,
                    previewBody = entity.previewBody,
                    previewTimestamp = entity.previewTimestamp,
                )
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
                else Conversation(
                    identifier = entity.identifier,
                    picture = entity.picture,
                    previewTitle = entity.name,
                    previewBody = entity.previewBody,
                    previewTimestamp = entity.previewTimestamp,
                )
            }
        }

    override fun messages(
        other: FirebaseUid,
    ): Flow<List<Message>> = database.messages()
        .messages(other)
        .map {
            it.map { msg ->
                Message(
                    identifier = msg.identifier,
                    body = msg.body,
                    timestamp = msg.timestamp,
                    from = if (msg.from == other) Sender.Other else Sender.Myself,
                    sent = true,
                )
            }
        }

    override suspend fun send(
        to: FirebaseUid,
        message: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun accept(match: PendingMatch) {
        database.conversations().accept(match.identifier)
    }
}
