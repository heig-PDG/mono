package tupperdate.web.model.profiles

import tupperdate.web.facade.PictureUrl
import tupperdate.web.facade.profiles.Profile

data class ModelUser(
    val identifier: String,
    val displayName: String,
    val displayPicture: PictureUrl?,
    val lastSeenRecipe: Long,
)

fun ModelUser.toProfile(phone: String): Profile = Profile(
    identifier = this.identifier,
    displayName = this.displayName,
    picture = this.displayPicture,
    phone = phone,
)
