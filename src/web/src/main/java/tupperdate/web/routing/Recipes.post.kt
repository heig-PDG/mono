package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.model.toRecipe
import tupperdate.web.model.toRecipeDTO
import tupperdate.web.util.await

/**
 * Posts a [NewRecipeDTO] to the database, and returns the built [RecipeDTO].
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.recipesPost(store: Firestore) = post {
    val uid = requireNotNull(call.firebaseAuthPrincipal?.uid)
    val doc = store.collection("users").document(uid).collection("recipes").document()
    val recipe = call.receive<NewRecipeDTO>().toRecipe(doc.id, "https://thispersondoesnotexist.com/image")

    // TODO (alex) : See how we can handle exceptions.
    doc.set(recipe).await()

    call.respond(HttpStatusCode.OK, recipe.toRecipeDTO())
}
