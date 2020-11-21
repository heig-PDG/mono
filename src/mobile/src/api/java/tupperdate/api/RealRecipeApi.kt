package tupperdate.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class RealRecipeApi(private val auth: RealAuthenticationApi) : RecipeApi {

    override fun like(recipe: RecipeApi.Recipe) {
    }

    override fun dislike(recipe: RecipeApi.Recipe) {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun stack(): Flow<List<RecipeApi.Recipe>> {
        return auth.auth
            .filterNotNull()
            // TODO : Actually fetch from the API here.
            .flatMapLatest { emptyFlow() }
    }

    override val backStackEnabled: Flow<Boolean>
        get() = flowOf(false)

    override fun back() {
    }

    override suspend fun create(title: String, description: String) {
    }
}
