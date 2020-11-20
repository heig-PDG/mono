package tupperdate.api

import kotlinx.coroutines.flow.Flow

interface RecipeApi {

    data class Recipe(
        val title: String,
        val description: String,
        val pictureUrl: String,
    )

    fun like(recipe: Recipe)
    fun dislike(recipe: Recipe)

    fun stack(): Flow<List<Recipe>>

    val backStackEnabled: Flow<Boolean>

    fun back()

    suspend fun create(title: String, description: String)
}
