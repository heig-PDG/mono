package tupperdate.web.routing.chats

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
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

fun Route.postMessage(auth: FirebaseAuth) = post {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)

    call.respond(HttpStatusCode.OK)
}
