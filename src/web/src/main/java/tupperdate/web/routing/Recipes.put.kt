package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.model.Chat
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.BadRequestException
import tupperdate.web.exceptions.ForbiddenException
import tupperdate.web.exceptions.UnauthorizedException
import tupperdate.web.util.await

/**
 * Post a like to a recipe as an authenticated user
 *
 * @param store the [Firestore] instance that is used.
 */
fun Route.recipesPut(store: Firestore) {
    put("like/{recipeId}") {
        try {
            val recipes = store.collectionGroup("recipes")
            val chats = store.collection("chats")
            val recipeId = call.parameters["recipeId"] ?: throw BadRequestException()

            var userId1 = call.firebaseAuthPrincipal?.uid ?: throw UnauthorizedException()
            var userId2 = recipes.whereEqualTo("id", recipeId).limit(1)
                .get().await().documents[0].reference.parent.id

            if (userId1 == userId2) throw ForbiddenException()

            // Order userId1 and userId2 in alphanumerical order (1 is inferior to 2)
            var callerIsUser1 = true
            if (userId1 > userId2) {
                callerIsUser1 = false
                userId1 = userId2.also { userId2 = userId1 }
            }

            // Get chat document or create it
            val chatDoc = chats.document(userId1 + "_" + userId2)

            //TODO: Firestore transaction
            var chatObject = chatDoc.get().await().toObject(Chat::class.java)
                ?: Chat(
                    id = chatDoc.id,
                    userId1 = userId1,
                    userId2 = userId2,
                    user1LikedRecipes = emptyList(),
                    user2LikedRecipes = emptyList(),
                )

            // add liked recipe to correct list
            chatObject =
                if (callerIsUser1) {
                    chatObject.copy(
                        user1LikedRecipes = (chatObject.user1LikedRecipes
                            ?: emptyList()) + recipeId
                    )
                } else {
                    chatObject.copy(
                        user2LikedRecipes = (chatObject.user2LikedRecipes
                            ?: emptyList()) + recipeId
                    )
                }

            chatDoc.set(chatObject)

            call.respond(HttpStatusCode.OK)
        } catch (exception: UnauthorizedException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (exception: BadRequestException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (exception: ForbiddenException) {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    put("dislike/{recipeId}") {
        call.respond(HttpStatusCode.NotImplemented)
    }
}

