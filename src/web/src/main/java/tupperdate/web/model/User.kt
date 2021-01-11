package tupperdate.web.model

import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO

data class User(
    val id: String? = null,
    val displayName: String? = null,
    val picture: String? = null,
    val lastSeenRecipe: Long? = null
)

fun MyUserDTO.toUser(id: String, picture: String?): User {
    return User(
        id = id,
        displayName = this.displayName,
        picture = picture,
        lastSeenRecipe = null,
    )
}

fun User.toUserDTO(): UserDTO {
    return UserDTO(
        id = requireNotNull(this.id),
        displayName = this.displayName,
        picture = this.picture,
    )
}
