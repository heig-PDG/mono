package tupperdate.web.facade.profiles

import tupperdate.common.dto.MyUserDTO
import tupperdate.web.facade.PictureBase64

data class NewProfile(
    val displayName: String,
    val picture: PictureBase64?,
)

fun MyUserDTO.toNewProfile(): NewProfile {
    return NewProfile(
        displayName = displayName,
        picture = imageBase64?.let(::PictureBase64)
    )
}
