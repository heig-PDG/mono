package tupperdate.web.model.recipes.firestore

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.StorageClient
import io.ktor.http.*
import org.apache.commons.codec.binary.Base64
import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User
import tupperdate.web.model.recipes.ModelNewRecipe
import tupperdate.web.model.recipes.ModelRecipe
import tupperdate.web.model.recipes.RecipeRepository
import tupperdate.web.model.recipes.toFirestoreRecipe
import tupperdate.web.utils.await
import java.util.*
import java.util.concurrent.TimeUnit

class FirestoreRecipeRepository(
    private val store: Firestore,
    private val storage: StorageClient,
) : RecipeRepository {

    override suspend fun save(
        user: User,
        recipe: ModelNewRecipe,
    ): Result<Unit> {
        val doc = store.collection("recipes").document()
        val id = UUID.randomUUID().toString()
        val bytes = recipe.picture?.let { Base64.decodeBase64(it.encoded) }
        var pict: String? = null

        if (bytes != null) {
            val fileName = "$id.jpg"
            val blob = storage.bucket().create(
                fileName,
                bytes.inputStream(),
                ContentType.Image.JPEG.contentType,
            )
            val url = blob.signUrl(365, TimeUnit.DAYS)
            pict = url.toString()
        }

        // TODO: Firestore transaction: For now, assume no two recipes are posted at the same time (bad assumption)
        val firestoreRecipe = recipe.toFirestoreRecipe(id = doc.id, userId = user.id.uid, pict?.let(::PictureUrl))
        return try {
            doc.set(firestoreRecipe).await()
            Result.Ok(Unit)
        } catch (throwable: Throwable) {
            Result.BadServer()
        }
    }

    override suspend fun readOwn(
        user: User,
    ): Result<List<ModelRecipe<PictureUrl>>> {
        TODO("Not yet implemented")
    }

    override suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<ModelRecipe<PictureUrl>>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<ModelRecipe<PictureUrl>> {
        TODO("Not yet implemented")
    }

    override suspend fun like(
        user: User,
        recipeId: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun dislike(
        user: User,
        recipeId: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}
