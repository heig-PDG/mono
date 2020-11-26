package tupperdate.web.routing.recipes

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.exceptions.statusException
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
    val countParam = call.parameters["count"] ?: statusException(HttpStatusCode.BadRequest)
    val count = countParam.toIntOrNull() ?: statusException(HttpStatusCode.BadRequest)

    // TODO (matt) : Filter already liked/disliked recipes
    val retrieved = store.collectionGroup("recipes").orderBy("timestamp")
        .limit(count).get().await()
    val recipes = retrieved.toObjects(Recipe::class.java)
    val dtos = recipes.map { it.toRecipeDTO() }

    call.respond(dtos)
}
