package tupperdate.web.model.profiles

import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.profiles.firestore.FirestoreUser

data class ModelUser(
    val identifier: String,
    val displayName: String,
    val displayPicture: PictureUrl?,
    val lastSeenRecipe: Long,
    val phone: String,
)

fun ModelUser.toFirestoreUser(): FirestoreUser = FirestoreUser(
    id = this.identifier,
    displayName = this.displayName,
    picture = this.displayPicture?.url,
    lastSeenRecipe = this.lastSeenRecipe,
)
