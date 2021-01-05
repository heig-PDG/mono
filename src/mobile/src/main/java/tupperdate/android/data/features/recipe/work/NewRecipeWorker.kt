package tupperdate.android.data.features.recipe.work

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.codec.binary.Base64
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO

@OptIn(KoinApiExtension::class)
@InternalDataApi
class NewRecipeWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client: HttpClient by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val base64Image = inputData.getString(RecipeImageBase64)
            ?.let(Uri::parse)
            ?.readFileAsBase64(applicationContext.contentResolver)

        val dto = NewRecipeDTO(
            title = requireNotNull(inputData.getString(RecipeTitle)),
            description = requireNotNull(inputData.getString(RecipeDescription)),
            attributes = RecipeAttributesDTO(
                vegetarian = inputData.getBoolean(RecipeIsVegetarian, false),
                warm = inputData.getBoolean(RecipeIsWarm, false),
                hasAllergens = inputData.getBoolean(RecipeHasAllergens, false),
            ),
            imageBase64 = base64Image,
        )

        return@withContext try {
            // TODO : Compress images.
            client.post<Unit>("/recipes") {
                body = dto
            }
            Result.success()
        } catch (problem: Throwable) {
            Result.retry()
        }
    }

    companion object {

        private const val RecipeTitle = "title"
        private const val RecipeDescription = "description"
        private const val RecipeIsVegetarian = "vegetarian"
        private const val RecipeIsWarm = "warm"
        private const val RecipeHasAllergens = "allergens"
        private const val RecipeImageBase64 = "picture"

        /**
         * Creates the [Data] associated with the creation of a [NewRecipe].
         */
        @InternalDataApi
        fun inputData(recipe: NewRecipe): Data {
            return workDataOf(
                RecipeTitle to recipe.title,
                RecipeDescription to recipe.description,
                RecipeIsVegetarian to recipe.isVegan,
                RecipeIsWarm to recipe.isWarm,
                RecipeHasAllergens to recipe.hasAllergens,
                RecipeImageBase64 to recipe.picture?.toString(),
            )
        }
    }
}

// TODO : Factorize this.
private fun Uri.readFileAsBase64(contentResolver: ContentResolver): String? =
    contentResolver.openInputStream(this)
        ?.buffered()
        ?.readBytes()
        ?.let(Base64::encodeBase64String)
