package tupperdate.web.legacy.routing.recipes

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.legacy.auth.tupperdateAuthPrincipal
import tupperdate.web.legacy.exceptions.statusException
import tupperdate.web.legacy.model.Recipe
import tupperdate.web.legacy.model.toRecipeDTO
import tupperdate.web.legacy.util.await

fun Route.recipesGet(store: Firestore) {
    own(store)
    some(store)
    all(store)
}

/**
 * Retrieves a [List] of all the [RecipeDTO] that the authenticated user has posted
 *
 * @param store the [Firestore] instance that is used.
 */
private fun Route.own(store: Firestore) = get("/own") {
    // Extract query params.
    val uid = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)

    val recipes = store.collection("recipes").whereEqualTo("userId", uid).get().await()
        .toObjects(Recipe::class.java).map { it.toRecipeDTO() }

    call.respond(recipes)
}

/**
 * Retrieves a single recipe from its identifier.
 *
 * @param store the [Firestore] instance that is used.
 */
private fun Route.some(store: Firestore) = get("/{identifier}") {
    val id = call.parameters["identifier"] ?: statusException(HttpStatusCode.BadRequest)

    val document = store.collection("recipes").document(id).get().await()
    val recipe = document.toObject(Recipe::class.java) ?: statusException(NotFound)
    call.respond(recipe.toRecipeDTO())
}

/**
 * Retrieves a [List] of all the [RecipeDTO] that a certain user has not yet liked or disliked.
 *
 * @param store the [Firestore] instance that is used.
 */
private fun Route.all(store: Firestore) = get {
    // Extract query params.
    val id = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val countParam = call.parameters["count"] ?: statusException(HttpStatusCode.BadRequest)
    val count = countParam.toIntOrNull() ?: statusException(HttpStatusCode.BadRequest)

    // TODO: Transaction
    val lastSeenRecipe = store.collection("users").document(id.uid)
        .get().await()
        .get("lastSeenRecipe") ?: 0

    // Filter user own recipes
    val filtered = mutableListOf<DocumentSnapshot>()

    var keepGoing = true
    var lastSnapshot: DocumentSnapshot? = null
    while (keepGoing) {
        val remaining = count - filtered.size
        val retrieved = when (lastSnapshot) {
            null -> store.collection("recipes")
                .whereGreaterThan("timestamp", lastSeenRecipe)
                .orderBy("timestamp")
                .limit(remaining)
                .get().await()
            else -> store.collection("recipes")
                .whereGreaterThan("timestamp", lastSeenRecipe)
                .orderBy("timestamp")
                .startAfter(lastSnapshot)
                .limit(remaining)
                .get().await()
        }
        filtered.addAll(retrieved.filter { it["userId"] != id })
        lastSnapshot = retrieved.lastOrNull()
        keepGoing = filtered.size < count && retrieved.size() == count && lastSnapshot != null
    }

    val recipes = filtered.mapNotNull { it.toObject(Recipe::class.java) }
    val dtos = recipes.map { it.toRecipeDTO() }

    call.respond(dtos)
}
