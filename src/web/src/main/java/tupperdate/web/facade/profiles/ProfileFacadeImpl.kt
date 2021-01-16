package tupperdate.web.facade.profiles

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tupperdate.web.model.Result
import tupperdate.web.model.accounts.Notification
import tupperdate.web.model.accounts.NotificationRepository
import tupperdate.web.model.accounts.PhoneRepository
import tupperdate.web.model.flatMap
import tupperdate.web.model.map
import tupperdate.web.model.profiles.*

class ProfileFacadeImpl(
    private val users: UserRepository,
    private val phones: PhoneRepository,
    private val notifications: NotificationRepository,
) : ProfileFacade {
    override suspend fun save(
        user: User,
        profileId: String,
        profile: NewProfile
    ): Result<Unit> {
        if (user.id.uid != profileId) return Result.Forbidden()
        val newUser = ModelNewUser(
            identifier = user.id.uid,
            displayName = profile.displayName,
            displayPicture = profile.picture,
        )

        val result = users.save(newUser)

        // Deferred non-blocking notification.
        if (result is Result.Ok) {
            GlobalScope.launch {
                notifications.send(
                    Notification
                        .ToUser
                        .UserSyncProfile(user.id.uid)
                )
            }
        }

        return result
    }

    override suspend fun update(
        user: User,
        profileId: String,
        partProfile: PartProfile
    ): Result<Unit> {
        if (user.id.uid != profileId) return Result.Forbidden()
        val partUser = partProfile.toModelPartUser(userId = user.id.uid)

        val result = users.update(partUser)

        // Deferred non-blocking notification.
        if (result is Result.Ok) {
            GlobalScope.launch {
                notifications.send(
                    Notification
                        .ToUser
                        .UserSyncProfile(user.id.uid)
                )
            }
        }

        return result
    }

    override suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile> {

        if (user.id.uid != profileId) return Result.Forbidden()

        return phones.read(user).flatMap { phone ->
            users.read(user).map { it.toProfile(phone.number) }
        }
    }

    override suspend fun register(
        user: User,
        token: NewNotificationToken,
    ) = users.register(user, token.toModelToken())
}
