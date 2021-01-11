package tupperdate.android.data.features.messages.impl

import androidx.work.WorkManager
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.messages.*
import tupperdate.android.data.features.messages.store.AllConversationFetcher
import tupperdate.android.data.features.messages.store.AllConversationSourceOfTruth
import tupperdate.android.data.features.messages.store.ConversationFetcher
import tupperdate.android.data.features.messages.store.ConversationSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

// TODO : Use some fetchers.
@InternalDataApi
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MessagesRepositoryImpl(
    private val database: TupperdateDatabase,
    private val manager: WorkManager,
    private val client: HttpClient,
) : MessagesRepository {

    private val allConversationsStore = StoreBuilder.from(
        fetcher = AllConversationFetcher(client),
        sourceOfTruth = AllConversationSourceOfTruth(database.messages()),
    ).build()

    private val singleConversationStore = StoreBuilder.from(
        fetcher = ConversationFetcher(client),
        sourceOfTruth = ConversationSourceOfTruth(database.messages()),
    ).build()

    override val pending: Flow<List<Match>> = allConversationsStore
        .stream(StoreRequest.cached(Unit, true))
        .map { it.dataOrNull() }
        .filterNotNull()
        .map {
            it.asSequence()
                .filter { conv -> !conv.acknowledged }
                .map { conversation ->
                    // TODO : Use some proper recipe pictures here.
                    Match(conversation.identifier, null, null)
                }
                .toList()
        }

    // TODO : Handle matches properly.

    override val matches: Flow<List<Match>> =
        emptyFlow()

    override val conversations: Flow<List<Conversation>> =
        allConversationsStore
            .stream(StoreRequest.cached(Unit, true))
            .map { it.dataOrNull() }
            .filterNotNull()
            .map {
                it.asSequence()
                    .filter { conv -> conv.acknowledged }
                    .toList()
            }

    override fun conversation(
        with: FirebaseUid,
    ): Flow<Conversation?> = singleConversationStore
        .stream(StoreRequest.cached(with, true))
        .map { it.dataOrNull() }

    override fun messages(
        other: FirebaseUid,
    ): Flow<List<Message>> = database
        .messages()
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

    override suspend fun accept(match: Match) {
        database.messages().accept(match.identifier)
    }
}
