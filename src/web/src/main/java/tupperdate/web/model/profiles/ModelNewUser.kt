package tupperdate.web.model.profiles

import tupperdate.web.facade.PictureBase64

data class ModelNewUser(
    val identifier: String,
    val displayName: String,
    val displayPicture: PictureBase64?,
)
