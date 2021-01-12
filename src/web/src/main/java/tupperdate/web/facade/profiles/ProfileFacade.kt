package tupperdate.web.facade.profiles

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ProfileFacade {

    suspend fun save(
        user: User,
        profileId: String,
        profile: Profile<PictureBase64>,
    ): Result<Profile<Unit>>

    suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile<PictureUrl>>
}
