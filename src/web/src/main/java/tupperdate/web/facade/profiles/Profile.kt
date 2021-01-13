package tupperdate.web.facade.profiles

import tupperdate.common.dto.UserDTO
import tupperdate.web.facade.PictureUrl

data class Profile(
    val identifier: String,
    val displayName: String,
    val picture: PictureUrl?,
    val phone: String,
)

fun Profile.toUserDTO() = UserDTO(
    id = identifier,
    phone = phone,
    displayName = displayName,
    picture = picture?.url,
)
