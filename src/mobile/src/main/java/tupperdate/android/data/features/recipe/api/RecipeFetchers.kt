package tupperdate.android.data.features.recipe.api

import com.dropbox.android.external.store4.Fetcher
import io.ktor.client.*
import io.ktor.client.request.*
import tupperdate.android.data.InternalDataApi
import tupperdate.common.dto.RecipeDTO

@InternalDataApi
object RecipeFetchers {

    private const val RecipesFetchCount = 4

    /**
     * A [Fetcher] that retrieves the most recent recipes for the currently logged in user. The
     * amount of fetched recipes should be provided as an argument.
     */
    fun allRecipesFetcher(client: HttpClient): Fetcher<Unit, List<RecipeDTO>> {
        return Fetcher.of {
            return@of client.get<List<RecipeDTO>>("/recipes") {
                parameter("count", RecipesFetchCount)
            }
        }
    }

    /**
     * A [Fetcher] that retrieves a single [RecipeDTO] for the currently logged in user.
     */
    fun singleRecipeFetcher(client: HttpClient): Fetcher<String, RecipeDTO> {
        return Fetcher.of {
            return@of client.get<RecipeDTO>("/recipes/$it")
        }
    }
}
