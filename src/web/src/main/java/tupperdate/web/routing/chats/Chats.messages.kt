package tupperdate.web.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.ConversationDTO
import tupperdate.common.dto.MessageDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.*
import tupperdate.web.util.await

fun Route.getMessages(store: Firestore) = get("/{userId}/messages") {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val userId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)

    val chatId = minOf(uid, userId) + "_" + maxOf(uid, userId)
    val chat = store.collection("chats").document(chatId).get().await().toObject(Chat::class.java) ?: statusException(HttpStatusCode.NotFound)

    if (chat.user1Recipes == null || chat.user2Recipes == null) {
        statusException(HttpStatusCode.NotFound)
    }

    val messages = store.collection("chats").document(chatId).collection("messages")
        .orderBy("timestamp", Query.Direction.DESCENDING).get().await()
        .toObjects(Message::class.java)

    val messageDTOs = messages.map { it.toMessageDTO() }

    call.respond(messageDTOs)
}
