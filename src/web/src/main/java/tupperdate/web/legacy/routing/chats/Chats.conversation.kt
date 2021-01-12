package tupperdate.web.legacy.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.ConversationDTO
import tupperdate.web.legacy.auth.firebaseAuthPrincipal
import tupperdate.web.legacy.exceptions.statusException
import tupperdate.web.legacy.model.*
import tupperdate.web.legacy.util.await

fun Route.getConvs(store: Firestore) = get("/{userId}") {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val userId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)

    val docId = minOf(uid, userId) + "_" + maxOf(uid, userId)

    val chat = store.collection("chats").document(docId).get().await().toObject(Chat::class.java) ?: statusException(HttpStatusCode.NotFound)

    if (chat.user1Recipes == null || chat.user2Recipes == null) {
        statusException(HttpStatusCode.NotFound)
    }

    val myRecipes = if (uid == chat.userId1) chat.user1Recipes else chat.user2Recipes
    val theirRecipes = if (uid != chat.userId1) chat.user1Recipes else chat.user2Recipes

    val conv = Conv(
        id = docId,
        myId = uid,
        theirId = userId,
        myRecipes = myRecipes,
        theirRecipes = theirRecipes,
    )

    val user = store.collection("users").document(userId).get().await()
        .toObject(User::class.java) ?: statusException(HttpStatusCode.InternalServerError)
    val recipes = store.collection("recipes")

    val convDTO = ConversationDTO(
        userId = userId,
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

    call.respond(convDTO)
}
