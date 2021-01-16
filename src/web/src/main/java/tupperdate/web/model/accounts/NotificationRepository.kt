package tupperdate.web.model.accounts

import tupperdate.web.model.Result

interface NotificationRepository {

    /**
     * Sends a specific [Notification]. Some notifications can be sent to individual users, whereas
     * others are sent to user groups.
     */
    suspend fun send(
        notification: Notification,
    ): Result<Unit>
}
