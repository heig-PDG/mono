package tupperdate.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.Flow

interface RecipeApi {

    @Parcelize
    data class Recipe(
        val title: String,
        val description: String,
        val pictureUrl: String,
    ): Parcelable

    fun like(recipe: Recipe)
    fun dislike(recipe: Recipe)

    fun stack(): Flow<List<Recipe>>

    val backStackEnabled: Flow<Boolean>

    fun back()

    suspend fun create(title: String, description: String)
}
