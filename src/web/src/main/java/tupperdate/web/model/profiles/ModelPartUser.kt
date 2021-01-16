package tupperdate.web.model.profiles

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.profiles.PartProfile

data class ModelPartUser(
    val identifier: String,
    val displayName: String?,
    val displayNameProvided: Boolean,
    val imageBase64: PictureBase64?,
    val imageBase64Provided: Boolean,
)

fun PartProfile.toModelPartUser(userId: String): ModelPartUser {
    return ModelPartUser(
        identifier = userId,
        displayName = displayName,
        displayNameProvided = displayNameProvided,
        imageBase64 = imageBase64,
        imageBase64Provided = imageBase64Provided,
    )
}
