package tupperdate.web.facade.profiles

import tupperdate.web.model.Result
import tupperdate.web.model.map
import tupperdate.web.model.profiles.*

class ProfileFacadeImpl(
    private val users: UserRepository,
) : ProfileFacade {

    override suspend fun save(
        user: User,
        profileId: String,
        profile: NewProfile
    ): Result<Unit> {
        if (user.id != profileId) return Result.Forbidden()
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
        if (user.id != profileId) return Result.Forbidden()

        return users.read(user).map(ModelUser::toProfile)
    }
}
