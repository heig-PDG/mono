package tupperdate.api

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import tupperdate.common.model.Recipe

class RealRecipeApi(private val auth: RealAuthenticationApi) : RecipeApi {

    private val client = HttpClient {
        install(JsonFeature)
        // TODO : Use some top-level variables, a build variant or some env variables.
        defaultRequest {
            // TODO : Use HTTPS.
            host = "api.tupperdate.me"
            port = 80
        }
    }

    override fun like(recipe: RecipeApi.Recipe) {
    }

    override fun dislike(recipe: RecipeApi.Recipe) {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun stack(): Flow<List<RecipeApi.Recipe>> {
        // TODO : Invalidate requests with server-sent notifications.
        return auth.auth
            .filterNotNull()
            .flatMapLatest { auth ->
                val recipes = client.get<List<Recipe>>("/recipes/4") {
                    header("Authorization", "Bearer ${auth.token}")
                }
                // TODO : Factorize this.
                val mapped = recipes.map {
                    RecipeApi.Recipe(
                        title = it.title ?: "",
                        description = it.title ?: "",
                        pictureUrl = it.title ?: "",
                    )
                }
                flowOf(mapped)
            }
    }

    override val backStackEnabled: Flow<Boolean>
        get() = flowOf(false)

    override fun back() {
    }

    override suspend fun create(title: String, description: String) {
    }
}
