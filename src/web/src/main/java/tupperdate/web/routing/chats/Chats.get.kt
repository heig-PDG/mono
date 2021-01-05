package tupperdate.web.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.ConversationDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.*
import tupperdate.web.util.await

fun Route.getChats(store: Firestore) = get {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)

    val smallerId = store.collection("chats").whereEqualTo("userId1", uid).get()
    val greaterId = store.collection("chats").whereEqualTo("userId2", uid).get()

    val chats = smallerId.await().toObjects(Chat::class.java).filter { it.userId2 != null } +
                greaterId.await().toObjects(Chat::class.java).filter { it.userId1 != null }

    val convs: List<Conv> = chats.map {
        val myId = if (uid == it.userId1) it.userId1 else it.userId2
        val theirId = if (uid != it.userId1) it.userId1 else it.userId2
        val myRecipes = if (uid == it.userId1) it.user1Recipes else it.user2Recipes
        val theirRecipes = if (uid != it.userId1) it.user1Recipes else it.user2Recipes

        return@map Conv(
            id = it.id ?: statusException(HttpStatusCode.InternalServerError),
            myId = myId ?: statusException(HttpStatusCode.InternalServerError),
            theirId = theirId ?: statusException(HttpStatusCode.InternalServerError),
            myRecipes = myRecipes ?: statusException(HttpStatusCode.InternalServerError),
            theirRecipes = theirRecipes ?: statusException(HttpStatusCode.InternalServerError),
        )
    }

    val conversationDTOS: List<ConversationDTO> = convs.map { conv ->
            val user = store.collection("users").document(conv.theirId).get().await()
                .toObject(User::class.java) ?: statusException(HttpStatusCode.InternalServerError)
            val recipes = store.collection("recipes")

            return@map ConversationDTO(
                userId = conv.theirId,
                displayName = user.displayName ?: statusException(HttpStatusCode.InternalServerError),
                picture = user.picture ?: statusException(HttpStatusCode.InternalServerError),
                lastMessage = store.collection("chats/${conv.id}/messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get().await()
                    .toObjects(Message::class.java).getOrNull(0)?.toMessageDTO(),
                myRecipes = conv.myRecipes.map {
                    recipes.document(conv.myId).get().await().toObject(Recipe::class.java) ?: statusException(HttpStatusCode.InternalServerError)
                }.map { it.toRecipeDTO() },
                theirRecipes = conv.theirRecipes.map {
                    recipes.document(conv.theirId).get().await().toObject(Recipe::class.java)
                        ?: statusException(HttpStatusCode.InternalServerError)
                }.map { it.toRecipeDTO() },
            )
        }

    call.respond(conversationDTOS)
}
