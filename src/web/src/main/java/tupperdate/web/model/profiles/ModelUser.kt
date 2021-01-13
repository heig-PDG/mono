package tupperdate.web.model.profiles

import tupperdate.web.model.Result
import tupperdate.web.facade.PictureUrl
import tupperdate.web.facade.profiles.Profile

data class ModelUser(
    val identifier: String,
    val displayName: String,
    val displayPicture: PictureUrl?,
    val lastSeenRecipe: Long,
    val phone: String,
)

fun Result<ModelUser>.toResultProfile(): Result<Profile> = when(this) {
    is Result.Ok<ModelUser> -> Result.Ok(this.result.toProfile())
    is Result.Forbidden<ModelUser> -> Result.Forbidden(this.error)
    is Result.BadInput<ModelUser> -> Result.BadInput(this.error)
    is Result.NotFound<ModelUser> -> Result.NotFound(this.error)
    is Result.MissingData<ModelUser> -> Result.MissingData(this.error)
    is Result.BadServer<ModelUser> -> Result.BadServer(this.error)
}

fun ModelUser.toProfile(): Profile = Profile(
    identifier = this.identifier,
    displayName = this.displayName,
    picture = this.displayPicture,
    phone = this.phone,
)
