package tupperdate.web.facade.profiles

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ProfileFacade {

    suspend fun save(
        user: User,
        profileId: String,
        profile: Profile<PictureBase64>,
    ): Result<Unit>

    suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile<PictureUrl>>
}
