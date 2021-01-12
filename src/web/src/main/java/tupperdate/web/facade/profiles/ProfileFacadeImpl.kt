package tupperdate.web.facade.profiles

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository

class ProfileFacadeImpl(
    private val users: UserRepository,
) : ProfileFacade {

    override suspend fun save(
        user: User,
        profileId: String,
        profile: Profile<PictureBase64>,
    ): Result<Profile<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile<PictureUrl>> {
        TODO("Not yet implemented")
    }
}
