package tupperdate.api

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import tupperdate.api.dto.asRecipe
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO

class RealRecipeApi(
    private val auth: RealAuthenticationApi,
    private val client: HttpClient,
) : RecipeApi {

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
                val recipes = client.get<List<RecipeDTO>>("/recipes") {
                    header("Authorization", "Bearer ${auth.token}")
                    parameter("count", 4) // TODO : Rename this to count
                }
                flowOf(recipes.map(RecipeDTO::asRecipe))
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
        hasAllergens: Boolean
    ) {
        // TODO : Handle exceptions.
        val auth = auth.auth.filterNotNull().first()
        client.post<NewRecipeDTO> {
            header("Authorization", "Bearer ${auth.token}")
            body = NewRecipeDTO(
                title = title,
                description = description,
                attributes = RecipeAttributesDTO(
                    vegetarian = vegetarian,
                    hasAllergens = hasAllergens,
                    warm = warm,
                )
            )
        }
    }
}
