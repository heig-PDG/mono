package tupperdate.web.routing.recipes

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
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.toRecipe
import tupperdate.web.model.toRecipeDTO
import tupperdate.web.util.await
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

    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val doc = store.collection("users").document(uid).collection("recipes").document()

    val dto = call.receive<NewRecipeDTO>()
    val id = UUID.randomUUID().toString()
    val bytes = dto.imageBase64?.let { Base64.decodeBase64(it) }
    // TODO : Don't add pictures for new recipes that don't provide any.
    var pict = "https://thispersondoesnotexist.com/image"

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

    val recipe = dto.toRecipe(doc.id, pict)

    doc.set(recipe).await()

    call.respond(recipe.toRecipeDTO())
}
