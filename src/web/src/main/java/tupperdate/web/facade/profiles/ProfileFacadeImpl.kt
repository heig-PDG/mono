package tupperdate.web.facade.profiles

import tupperdate.web.model.ForbiddenException
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.*

class ProfileFacadeImpl(
    private val users: UserRepository,
) : ProfileFacade {

    override suspend fun save(
        user: User,
        profileId: String,
        profile: NewProfile
    ): Result<Unit> {
        if (user.id != profileId) throw ForbiddenException()
        val newUser = ModelNewUser(
            identifier = user.id,
            displayName = profile.displayName,
            displayPicture = profile.picture,
        )

        return users.save(newUser)
    }

    override suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile> {
        if (user.id != profileId) return Result.Forbidden(ForbiddenException())

        return users.read(user).toResultProfile()
    }
}
