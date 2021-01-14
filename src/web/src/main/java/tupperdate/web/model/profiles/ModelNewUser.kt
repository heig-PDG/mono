package tupperdate.web.model.profiles

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.profiles.firestore.FirestoreUser

data class ModelNewUser(
    val identifier: String,
    val displayName: String,
    val displayPicture: PictureBase64?,
)

fun ModelNewUser.toFirestoreUser(
    picture: PictureUrl?,
    lastSeenRecipe: Long? = null,
): FirestoreUser = FirestoreUser(
    id = this.identifier,
    displayName = this.displayName,
    picture = picture?.url,
    lastSeenRecipe = lastSeenRecipe,
)
