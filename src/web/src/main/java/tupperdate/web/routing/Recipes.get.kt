package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.exceptions.BadRequestException
import tupperdate.web.model.Recipe
import tupperdate.web.model.toRecipeDTO
import tupperdate.web.util.await

/**
 * Retrieves a [List] of all the [RecipeDTO] that a certain user has not yet liked or disliked.
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.recipesGet(store: Firestore) = get {
    // Extract query params.
    // TODO (alex) : See how we can handle exceptions.
    val countParam = call.parameters["count"] ?: throw BadRequestException()
    val count = countParam.toIntOrNull() ?: throw BadRequestException()

    // TODO (matt) : Filter already liked/disliked recipes
    val retrieved = store.collectionGroup("recipes").orderBy("timestamp").limit(count).get().await()
    val recipes = retrieved.toObjects(Recipe::class.java)
    val dtos = recipes.map { it.toRecipeDTO() }

    call.respond(HttpStatusCode.OK, dtos)
}
