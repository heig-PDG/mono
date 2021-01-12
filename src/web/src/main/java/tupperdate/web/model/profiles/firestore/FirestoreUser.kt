package tupperdate.web.model.profiles.firestore

import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.MissingDataException
import tupperdate.web.model.profiles.ModelUser

data class FirestoreUser(
    val id: String? = null,
    val displayName: String? = null,
    val picture: String? = null,
    val lastSeenRecipe: Long? = null
)

fun FirestoreUser.toModelUser(phone: String): ModelUser {
    return ModelUser(
        identifier = this.id ?: throw MissingDataException(),
        displayName = this.displayName ?: throw MissingDataException(),
        displayPicture = picture?.let(::PictureUrl),
        lastSeenRecipe = this.lastSeenRecipe ?: 0,
        phone = phone,
    )
}

fun MyUserDTO.toUser(id: String, picture: String?): FirestoreUser {
    return FirestoreUser(
        id = id,
        displayName = this.displayName,
        picture = picture,
        lastSeenRecipe = null,
    )
}

fun FirestoreUser.toUserDTO(phone: String): UserDTO {
    return UserDTO(
        id = requireNotNull(this.id),
        phone = phone,
        displayName = this.displayName,
        picture = this.picture,
    )
}
