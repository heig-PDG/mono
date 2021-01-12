package tupperdate.web.model.profiles

import tupperdate.web.facade.PictureUrl

data class ModelUser(
    val identifier: String,
    val displayName: String,
    val displayPicture: PictureUrl?,
    val lastSeenRecipe: Long,
)
