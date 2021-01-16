package tupperdate.web.model.accounts

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tupperdate.web.model.Result

interface NotificationRepository {

    /**
     * Sends a specific [Notification]. Some notifications can be sent to individual users, whereas
     * others are sent to user groups.
     */
    suspend fun send(
        notification: Notification,
    ): Result<Unit>

    /**
     * Dispatches a group of [Notification] without looking at the result codes. The dispatch is
     * done asynchronously, with a fire-and-forget approach.
     *
     * @param notification the [Notification] instances to be sent.
     */
    fun dispatch(vararg notification: Notification) {
        GlobalScope.launch {
            for (message in notification) {
                launch { send(message) }
            }
        }
    }
}
