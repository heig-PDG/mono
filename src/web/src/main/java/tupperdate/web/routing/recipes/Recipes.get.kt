package tupperdate.web.routing.recipes

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.Recipe
import tupperdate.web.model.toRecipeDTO
import tupperdate.web.util.await

fun Route.recipesGet(store: Firestore) {
    some(store)
    all(store)
}

/**
 * Retrieves a single recipe from its identifier.
 *
 * @param store the [Firestore] instance that is used.
 */
private fun Route.some(store: Firestore) = get("/{identifier}") {
    val id = call.parameters["identifier"] ?: statusException(HttpStatusCode.BadRequest)

    val document = store.collection("recipes").document(id).get().await()
    val recipe = document.toObject(Recipe::class.java) ?: statusException(InternalServerError)
    call.respond(recipe.toRecipeDTO())
}

/**
 * Retrieves a [List] of all the [RecipeDTO] that a certain user has not yet liked or disliked.
 *
 * @param store the [Firestore] instance that is used.
 */
private fun Route.all(store: Firestore) = get {
    // Extract query params.
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val countParam = call.parameters["count"] ?: statusException(HttpStatusCode.BadRequest)
    val count = countParam.toIntOrNull() ?: statusException(HttpStatusCode.BadRequest)

    // TODO: Transaction
    val lastSeenRecipe =
        store.collection("users").document(uid).get().await().get("lastSeenRecipe") ?: 0

    // TODO: Filter user own recipes
    val retrieved = store.collection("recipes").whereGreaterThan("timestamp", lastSeenRecipe)
        .orderBy("timestamp").limit(count).get().await()

    val recipes = retrieved.toObjects(Recipe::class.java)
    val dtos = recipes.map { it.toRecipeDTO() }

    call.respond(dtos)
}
