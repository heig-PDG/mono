package tupperdate.web.routing.chats

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.MessageContentDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.statusException
import tupperdate.web.model.Chat
import tupperdate.web.model.Message
import tupperdate.web.model.toMessageDTO
import tupperdate.web.util.await

fun Route.postMessage(store: Firestore) = post("/{userId}/messages") {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val userId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)

    val content = call.receive<MessageContentDTO>()

    val chatId = minOf(uid, userId) + "_" + maxOf(uid, userId)
    val chat = store.collection("chats").document(chatId).get().await().toObject(Chat::class.java) ?: statusException(HttpStatusCode.NotFound)

    if (chat.user1Recipes == null || chat.user2Recipes == null) {
        statusException(HttpStatusCode.NotFound)
    }
    val newDoc = store.collection("chats").document(chatId).collection("messages").document()
    val message = Message(
        id = newDoc.id,
        content = content.content,
        timestamp = System.currentTimeMillis(),
        fromUser = uid
    )

    newDoc.set(message).await()

    call.respond(message.toMessageDTO())
}
