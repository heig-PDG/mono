package tupperdate.web.model.profiles.firestore

import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.profiles.ModelUser

data class FirestoreUser(
    val id: String? = null,
    val displayName: String? = null,
    val picture: String? = null,
    val lastSeenRecipe: Long? = null
)

fun FirestoreUser.toModelUser(): ModelUser? {
    return ModelUser(
        identifier = this.id ?: return null,
        displayName = this.displayName ?: return null,
        displayPicture = picture?.let(::PictureUrl),
        lastSeenRecipe = this.lastSeenRecipe ?: 0,
    )
}
