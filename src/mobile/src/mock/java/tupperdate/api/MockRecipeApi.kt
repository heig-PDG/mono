package tupperdate.api

import android.net.Uri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

object MockRecipeApi : RecipeApi {

    private fun randomRecipe(): RecipeApi.Recipe {
        // TODO : Make this funnier. Introduce (opaque) identifiers to recipes.
        return RecipeApi.Recipe(
            title = "Red lobster ${Random.nextInt()}",
            description = "This is a wonderful recipe.",
            pictureUrl = "https://via.placeholder.com/450"
        )
    }

    override fun like(recipe: RecipeApi.Recipe) {
    }

    override fun dislike(recipe: RecipeApi.Recipe) {
    }

    override fun stack(): Flow<List<RecipeApi.Recipe>> = flow {
        val items = mutableListOf<RecipeApi.Recipe>()
        repeat(5) {
            delay(1000)
            items += randomRecipe()
            emit(items.toList())
        }
    }

    override val backStackEnabled: Flow<Boolean>
        get() = flowOf(false)

    override fun back() {
    }

    override suspend fun create(
        title: String,
        description: String,
        vegetarian: Boolean,
        warm: Boolean,
        hasAllergens: Boolean,
        imageUri: Uri?,
    ) {
    }
}
