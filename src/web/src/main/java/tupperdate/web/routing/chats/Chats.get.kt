package tupperdate.web.routing.chats

import com.google.cloud.firestore.Firestore
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

    val convs = chats.map {
        Conv(
            id = it.id ?: statusException(HttpStatusCode.InternalServerError),
            myId = if (uid != it.userId1) it.userId2 ?: statusException(HttpStatusCode.InternalServerError) else it.userId1,
            theirId = it.userId2 ?: statusException(HttpStatusCode.InternalServerError),
            myRecipes = it.user1LikedRecipes ?: statusException(HttpStatusCode.InternalServerError),
            theirRecipes = it.user2LikedRecipes ?: statusException(HttpStatusCode.InternalServerError),
        )
    }


    val conversationDTOS = convs.map {
        ConversationDTO(
            userId = it.theirId,
            displayName = store.collection("users").document(it.theirId).get().await().toObject(User::class.java)?.displayName ?: statusException(HttpStatusCode.InternalServerError),
            picture = store.collection("users").document(it.theirId).get().await().toObject(User::class.java)?.picture ?: statusException(HttpStatusCode.InternalServerError),
            lastMessage = null,
            myRecipes = it.myRecipes.map { store.collection("recipes").document(it).get().await().toObject(Recipe::class.java) ?: statusException(HttpStatusCode.InternalServerError) }.map { it.toRecipeDTO() },
            theirRecipes = it.theirRecipes.map { store.collection("recipes").document(it).get().await().toObject(Recipe::class.java) ?: statusException(HttpStatusCode.InternalServerError) }.map { it.toRecipeDTO() },
        )
    }

    call.respond(HttpStatusCode.OK)
}
