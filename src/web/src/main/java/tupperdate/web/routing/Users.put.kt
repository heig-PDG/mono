package tupperdate.web.routing

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.cloud.StorageClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.apache.commons.codec.binary.Base64
import tupperdate.common.dto.MyUserDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.toUser
import tupperdate.web.util.await
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Puts a new [MyUserDTO] to the database.
 *
 * @param firebase the [FirebaseApp] instance that is used.
 */
fun Route.usersPut(firebase: FirebaseApp) = put("{userId}") {
    val store = FirestoreClient.getFirestore(firebase)
    val bucket = StorageClient.getInstance(firebase).bucket()

    val pathUserId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)
    val authUserId = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    if (authUserId != pathUserId) throw statusException(HttpStatusCode.Forbidden)

    val doc = store.collection("users").document(authUserId)
    val dto = call.receive<MyUserDTO>()
    val id = UUID.randomUUID().toString()
    val bytes = dto.imageBase64?.let { Base64.decodeBase64(it) }
    var pict : String? = null

    if (bytes != null) {
        val fileName = "$id.jpg"
        val blob = bucket.create(
            fileName,
            bytes.inputStream(),
            ContentType.Image.JPEG.contentType,
        )
        // TODO: Find alternative to timeout
        val url = blob.signUrl(365, TimeUnit.DAYS)
        pict = url.toString()
    }

    val user = dto.toUser(doc.id, pict)

    doc.set(user).await()
    call.respond(HttpStatusCode.OK)
}
