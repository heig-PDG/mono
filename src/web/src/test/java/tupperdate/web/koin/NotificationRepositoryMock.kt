package tupperdate.web.koin

import tupperdate.web.model.Result
import tupperdate.web.model.accounts.Notification
import tupperdate.web.model.accounts.NotificationRepository

class NotificationRepositoryMock : NotificationRepository {
    override suspend fun send(
        notification: Notification,
    ): Result<Unit> {
        return Result.Ok(Unit)
    }
}
