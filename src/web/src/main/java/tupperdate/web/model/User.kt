package tupperdate.web.model

import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

data class User(
    val id: String? = null,
    val displayName: String? = null,
    val phone: String? = null,
    val picture: String? = null,
    val fcmTokens: List<String> = emptyList(),
)

fun MyUserDTO.toUser(id: String, phone: String, picture: String): User {
    return User(
        id = id,
        displayName = this.displayName,
        phone = phone,
        picture = picture,
    )
}

fun User.toUserDTO(): UserDTO {
    // TODO: Fix default value problem
    return UserDTO(
        id = requireNotNull(this.id),
        displayName = this.displayName ?: "",
        phone = this.phone ?: "",
        picture = this.picture ?: "",
    )
}
