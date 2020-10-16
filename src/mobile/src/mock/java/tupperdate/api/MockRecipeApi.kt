package tupperdate.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

object MockRecipeApi : RecipeApi {

    override fun like(recipe: RecipeApi.Recipe) {
    }

    override fun dislike(recipe: RecipeApi.Recipe) {
    }

    override fun stack(): Flow<List<RecipeApi.Recipe>> {
        return emptyFlow()
    }

    override val backStackEnabled: Flow<Boolean>
        get() = flowOf(false)

    override fun back() {
    }

    override suspend fun create(title: String, description: String) {
    }
}