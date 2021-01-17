package tupperdate.android.data.features.recipe

import android.net.Uri

data class UpdateRecipe(
    val id: String,
    val title: String,
    val titleUpdate: Boolean,
    val description: String,
    val descriptionUpdate: Boolean,
    val picture: Uri?,
    val pictureUpdate: Boolean,

    // Recipe attributes.
    val isVegan: Boolean,
    val isVeganUpdate: Boolean,
    val isWarm: Boolean,
    val isWarmUpdate: Boolean,
    val hasAllergens: Boolean,
    val hasAllergensUpdate: Boolean,
) {

    fun hasUpdates() = titleUpdate
            || descriptionUpdate
            || pictureUpdate
            || isVeganUpdate
            || isWarmUpdate
            || hasAllergensUpdate
}
