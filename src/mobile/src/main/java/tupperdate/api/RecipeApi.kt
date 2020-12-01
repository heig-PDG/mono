package tupperdate.api

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.Flow
import java.util.*

interface RecipeApi {

    @Parcelize
    data class Recipe(
        val recipeId: String=UUID.randomUUID().toString(),
        val title: String,
        val description: String,
        val pictureUrl: String,
    ): Parcelable

    suspend fun like(recipe: Recipe)
    suspend fun dislike(recipe: Recipe)

    fun stack(): Flow<List<Recipe>>

    val backStackEnabled: Flow<Boolean>

    fun back()

    suspend fun create(
        title: String,
        description: String,
        vegetarian: Boolean,
        warm: Boolean,
        hasAllergens: Boolean,
        imageUri: Uri?,
    )
}
