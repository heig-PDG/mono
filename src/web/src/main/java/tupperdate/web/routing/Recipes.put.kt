package tupperdate.web.routing

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
import tupperdate.web.util.await


/**
 * Post a like to a recipe as an authenticated user
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.recipesPut(store: Firestore) {
    put("{recipeId}/like") {
        val recipeId = call.parameters["recipeId"] ?: statusException(HttpStatusCode.BadRequest)
        val recipeDoc = store.collectionGroup("recipes").whereEqualTo("id", recipeId).limit(1)
            .get().await().documents[0].reference

        var userId1 = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
        var userId2 = recipeDoc.parent.parent?.id ?: statusException(HttpStatusCode.Conflict)

        // A user can't like his own recipe
        if (userId1 == userId2) statusException(HttpStatusCode.Forbidden)

        // Order userId1 and userId2 in alphanumerical order (1 is inferior to 2)
        var callerIsUser1 = true
        if (userId1 > userId2) {
            callerIsUser1 = false
            userId1 = userId2.also { userId2 = userId1 }
        }

        val chatDoc = store.collection("chats").document(userId1 + "_" + userId2)

        // Create or update chatObject
        //TODO: Firestore transaction
        var chat = chatDoc.get().await().toObject(Chat::class.java)
            ?: Chat(
                id = chatDoc.id,
                userId1 = userId1,
                userId2 = userId2,
            )
        chatDoc.set(chat, SetOptions.merge())

        // set recipe as liked
        chatDoc.update(
            mapOf(
                "user1LikedRecipes" to FieldValue.arrayUnion(if (callerIsUser1) recipeId else null),
                "user2LikedRecipes" to FieldValue.arrayUnion(if (!callerIsUser1) recipeId else null),
            )
        )

        // set recipe as seen
        // TODO: Remodel the concept of seen recipes using a timestamp sorted stack
        // recipeDoc.update("seen", FieldValue.arrayUnion((userId1 to true)))

        call.respond(HttpStatusCode.NotImplemented)
    }

    put("{recipeId}/dislike") {
        /*
        val recipeId = call.parameters["recipeId"] ?: statusException(HttpStatusCode.BadRequest)
        var userId1 =
            call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
        var doc = store.collectionGroup("recipes").whereEqualTo("id", recipeId).limit(1)
            .get().await().documents[0].reference

        TODO: Remodel the concept of seen recipes using a timestamp sorted stack
        doc.update("seen", FieldValue.arrayUnion((userId1 to true)))
        */
        call.respond(HttpStatusCode.NotImplemented)
    }
}

