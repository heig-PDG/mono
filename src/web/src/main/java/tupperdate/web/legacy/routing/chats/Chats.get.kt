package tupperdate.web.legacy.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.ConversationDTO
import tupperdate.web.utils.auth.tupperdateAuthPrincipal
import tupperdate.web.legacy.model.*
import tupperdate.web.legacy.util.await
import tupperdate.web.model.profiles.firestore.FirestoreUser
import tupperdate.web.statusException

fun Route.getChats(store: Firestore) = get {
    val id = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)

    val smallerId = store.collection("chats").whereEqualTo("userId1", id).get()
    val greaterId = store.collection("chats").whereEqualTo("userId2", id).get()

    val chats = (smallerId.await().toObjects(Chat::class.java) + greaterId.await()
        .toObjects(Chat::class.java))
        .filter { it.user1Recipes != null && it.user2Recipes != null }

    val convs: List<Conv> = chats.map {
        val myId = if (id.uid == it.userId1) it.userId1 else it.userId2
        val theirId = if (id.uid != it.userId1) it.userId1 else it.userId2
        val myRecipes = if (id.uid == it.userId1) it.user1Recipes else it.user2Recipes
        val theirRecipes = if (id.uid != it.userId1) it.user1Recipes else it.user2Recipes

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
            .toObject(FirestoreUser::class.java)
            ?: statusException(HttpStatusCode.InternalServerError)
        val recipes = store.collection("recipes")

        return@map ConversationDTO(
            userId = conv.theirId,
            displayName = user.displayName ?: statusException(HttpStatusCode.InternalServerError),
            picture = user.picture ?: "https://thispersondoesnotexist.com/", // TODO: Fix me
            lastMessage = store.collection("chats").document(conv.id).collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get().await()
                .toObjects(Message::class.java).getOrNull(0)?.toMessageDTO(),
            myRecipes = conv.myRecipes.map {
                recipes.document(it).get().await().toObject(Recipe::class.java)
                    ?: statusException(HttpStatusCode.InternalServerError)
            }.map { it.toRecipeDTO() },
            theirRecipes = conv.theirRecipes.map {
                recipes.document(it).get().await().toObject(Recipe::class.java)
                    ?: statusException(HttpStatusCode.InternalServerError)
            }.map { it.toRecipeDTO() },
        )
    }

    call.respond(conversationDTOS)
}
