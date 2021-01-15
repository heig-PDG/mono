package tupperdate.web.legacy.routing.recipes

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.legacy.model.Recipe
import tupperdate.web.legacy.model.toRecipeDTO
import tupperdate.web.utils.await
import tupperdate.web.utils.tupperdateAuthPrincipal
import tupperdate.web.utils.statusException

fun Route.recipesGet(store: Firestore) {
    all(store)
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
        filtered.addAll(retrieved.filter { it["userId"] != id.uid })
        lastSnapshot = retrieved.lastOrNull()
        keepGoing = filtered.size < count && retrieved.size() == count && lastSnapshot != null
    }

    val recipes = filtered.mapNotNull { it.toObject(Recipe::class.java) }
    val dtos = recipes.map { it.toRecipeDTO() }

    call.respond(dtos)
}
