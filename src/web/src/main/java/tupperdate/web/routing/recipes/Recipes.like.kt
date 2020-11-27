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

        fun smallerId() = if (callerId < userId) callerId else userId
        fun greaterId() = if (callerId < userId) userId else callerId
        fun smallerIdLiked() = if (callerId < userId) recipeId else null
        fun greaterIdLiked() = if (callerId < userId) null else recipeId

        val chatDoc = store.collection("chats").document(smallerId() + "_" + greaterId())

        //TODO: Firestore transaction

        // Create or update chatObject
        val chat = Chat(id = chatDoc.id, userId1 = smallerId(), userId2 = greaterId())
        val likesToAdd = mapOf(
            "user1LikedRecipes" to FieldValue.arrayUnion(smallerIdLiked()),
            "user2LikedRecipes" to FieldValue.arrayUnion(greaterIdLiked()),
        )

        // Set unchanging chat fields (if they don't already exist)
        chatDoc.set(chat, SetOptions.merge())
        // Append recipe likes to array
        chatDoc.update(likesToAdd)

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

