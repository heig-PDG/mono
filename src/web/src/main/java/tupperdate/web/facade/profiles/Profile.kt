package tupperdate.web.facade.profiles

import tupperdate.web.facade.PictureUrl

data class Profile(
    val identifier: String,
    val displayName: String,
    val picture: PictureUrl?,
    val phone: String,
)
