package tupperdate.web.model.profiles.firestore

import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.MissingData
import tupperdate.web.model.profiles.ModelUser

data class FirestoreUser(
    val id: String? = null,
    val displayName: String? = null,
    val picture: String? = null,
    val lastSeenRecipe: Long? = null
)

fun FirestoreUser.toModelUser(phone: String): ModelUser {
    return ModelUser(
        identifier = this.id ?: throw MissingData(),
        displayName = this.displayName ?: throw MissingData(),
        displayPicture = picture?.let(::PictureUrl),
        lastSeenRecipe = this.lastSeenRecipe ?: 0,
        phone = phone,
    )
}
