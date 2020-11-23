package tupperdate.api

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import tupperdate.api.dto.asRecipe
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO

class RealRecipeApi(
    private val client: HttpClient,
) : RecipeApi {

    override fun like(recipe: RecipeApi.Recipe) {
    }

    override fun dislike(recipe: RecipeApi.Recipe) {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun stack(): Flow<List<RecipeApi.Recipe>> {
        // TODO : Invalidate requests with server-sent notifications.
        return flow {
            val recipes = client.get<List<RecipeDTO>>("/recipes") {
                parameter("count", 4)
            }
            emit(recipes.map(RecipeDTO::asRecipe))
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
        client.post<Unit>("/recipes") {
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
