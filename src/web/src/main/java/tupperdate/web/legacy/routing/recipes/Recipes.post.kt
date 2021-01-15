package tupperdate.web.legacy.routing.recipes

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.cloud.StorageClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.apache.commons.codec.binary.Base64
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.legacy.model.toRecipe
import tupperdate.web.legacy.model.toRecipeDTO
import tupperdate.web.utils.await
import tupperdate.web.utils.tupperdateAuthPrincipal
import tupperdate.web.utils.statusException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Posts a [NewRecipeDTO] to the database, and returns the built [RecipeDTO].
 *
 * @param firebase the [FirebaseApp] instance that is used.
 */
fun Route.recipesPost(firebase: FirebaseApp) = post {

    val store = FirestoreClient.getFirestore(firebase)
    val bucket = StorageClient.getInstance(firebase).bucket()

    val callerId = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val dto = call.receive<NewRecipeDTO>()

    val doc = store.collection("recipes").document()

    val id = UUID.randomUUID().toString()
    val bytes = dto.imageBase64?.let { Base64.decodeBase64(it) }

    var pict: String? = null

    if (bytes != null) {
        val fileName = "$id.jpg"
        val blob = bucket.create(
            fileName,
            bytes.inputStream(),
            ContentType.Image.JPEG.contentType,
        )
        val url = blob.signUrl(365, TimeUnit.DAYS)
        pict = url.toString()
    }

    // TODO: Firestore transaction
    // For now, assume no two recipes are posted at the same time (bad assumption)
    val recipe = dto.toRecipe(doc.id, callerId.uid, pict)
    doc.set(recipe).await()

    call.respond(recipe.toRecipeDTO())
}
