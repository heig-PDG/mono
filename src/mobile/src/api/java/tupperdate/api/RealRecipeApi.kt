package tupperdate.api

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.apache.commons.codec.binary.Base64
import tupperdate.api.dto.asRecipe
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO

class RealRecipeApi(
    private val client: HttpClient,
    private val activity: AppCompatActivity,
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
        hasAllergens: Boolean,
        imageUri: Uri?,
    ) {
        try {
            client.post<Unit>("/recipes") {
                // TODO : Improve client-side image handling.
                // TODO : Compress images.
                val imageData = makeBase64(imageUri)

                body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        vegetarian = vegetarian,
                        hasAllergens = hasAllergens,
                        warm = warm,
                    ),
                    imageBase64 = imageData,
                )
            }
        } catch (problem: Throwable) {
            problem.printStackTrace()
        }
    }

    private fun makeBase64(uri: Uri?) : String? {
        if (uri == null)
            return null

        val file = uri.toFile()
        return if (file.exists()) {
            val array = file.inputStream().buffered().readBytes()
            Base64.encodeBase64String(array)
        } else {
            null
        }
    }
}
