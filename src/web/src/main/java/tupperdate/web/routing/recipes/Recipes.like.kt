package tupperdate.web.routing.recipes

import com.google.cloud.firestore.FieldValue
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.Chat
import tupperdate.web.model.NewChat
import tupperdate.web.model.Recipe
import tupperdate.web.util.await


/**
 * Post a like to a recipe as an authenticated user
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.recipesPut(store: Firestore) {
    put("{recipeId}/like") {
        val recipeId = call.parameters["recipeId"] ?: statusException(HttpStatusCode.BadRequest)
        val recipe = store.collection("recipes").document(recipeId).get().await()
            .toObject(Recipe::class.java) ?: statusException(HttpStatusCode.NotFound)

        val callerId = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
        val userId = recipe.userId ?: statusException(HttpStatusCode.NotFound)

        val userDoc = store.collection("users").document(callerId)

        // A user can't like his own recipe
        if (callerId == userId) statusException(HttpStatusCode.Forbidden)

        fun smallerId() = minOf(callerId, userId)
        fun greaterId() = maxOf(callerId, userId)
        fun callerLike() = if (userId < callerId) "user1Recipes" else "user2Recipes"

        val chatDoc = store.collection("chats").document(smallerId() + "_" + greaterId())

        // TODO: Firestore transaction
        // Set base data but don't touch likes arrays
        val chat = NewChat(id = chatDoc.id, userId1 = smallerId(), userId2 = greaterId())
        chatDoc.set(chat, SetOptions.merge())
        // Append recipe likes to correct array
        chatDoc.update(mapOf(callerLike() to FieldValue.arrayUnion(recipeId)))

        // set recipe as seen
        val time = recipe.timestamp ?: statusException(HttpStatusCode.NotFound)
        userDoc.update("lastSeenRecipe", time)

        call.respond(HttpStatusCode.OK)
    }

    put("{recipeId}/dislike") {
        val recipeId = call.parameters["recipeId"] ?: statusException(HttpStatusCode.BadRequest)
        val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)

        val userDoc = store.collection("users").document(uid)
        val recipeDoc = store.collection("recipes").document(recipeId)

        val time = recipeDoc.get().await().toObject(Recipe::class.java)?.timestamp ?: statusException(HttpStatusCode.NotFound)
        userDoc.update("lastSeenRecipe", time)

        call.respond(HttpStatusCode.OK)
    }
}

