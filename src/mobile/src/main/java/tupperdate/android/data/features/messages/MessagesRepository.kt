package tupperdate.android.data.features.messages

import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.features.auth.firebase.FirebaseUid

/**
 * A repository that stores messages, conversations and matches. Conversations are separated as
 * follows:
 *
 * - [matches] are displayed for users with whom we've matched, but have not yet had any
 *   conversation and message exchanges.
 *
 * - [conversations] are displayed for users with whom we've matched, and at least one message has
 *   been exchanged at some point.
 */
interface MessagesRepository {

    /**
     * Returns a [Flow] of all the matches for the currently logged in user which have not been
     * accepted yet. When a new match occurs, it will be displayed with a "new match" popup until
     * it is not [pending] anymore.
     */
    val pending: Flow<List<PendingMatch>>

    /**
     * Returns a [Flow] of all the matches for the currently logged in user. This includes all the
     * [pending] matches.
     */
    val matches: Flow<List<Match>>

    /**
     * Returns a [Flow] of all the conversations for the currently logged in user.
     */
    val conversations: Flow<List<Conversation>>

    /**
     * Returns a [Flow] of the [Conversation] information with a specific user.
     */
    fun conversation(with: FirebaseUid): Flow<Conversation?>

    /**
     * Returns a [Flow] of the [ConversationInfo] information with a specific user.
     */
    fun conversationInfo(with: FirebaseUid): Flow<ConversationInfo?>

    /**
     * Returns a [Flow] of all the [messages] from a conversation with a specific user. These
     * messages will automatically get updated over time, as new data is generated on the server.
     */
    fun messages(other: FirebaseUid): Flow<List<Message>>

    /**
     * Sends a new [String] message to the user with the given [FirebaseUid].
     */
    suspend fun send(to: FirebaseUid, message: String)

    /**
     * Accepts the given [Match], and acknowledges it locally.
     */
    suspend fun accept(match: PendingMatch)
}
