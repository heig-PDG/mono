package tupperdate.web.legacy.routing.chats

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.MessageContentDTO
import tupperdate.web.legacy.auth.tupperdateAuthPrincipal
import tupperdate.web.legacy.exceptions.statusException
import tupperdate.web.legacy.model.Chat
import tupperdate.web.legacy.model.Message
import tupperdate.web.legacy.model.toMessageDTO
import tupperdate.web.legacy.util.await

fun Route.postMessage(store: Firestore) = post("/{userId}/messages") {
    val id = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val userId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)

    val messageContent = call.receive<MessageContentDTO>()

    val chatId = minOf(id.uid, userId) + "_" + maxOf(id.uid, userId)
    val chat = store.collection("chats").document(chatId).get().await().toObject(Chat::class.java)
        ?: statusException(HttpStatusCode.NotFound)

    if (chat.user1Recipes == null || chat.user2Recipes == null) {
        statusException(HttpStatusCode.NotFound)
    }
    val newDoc = store.collection("chats").document(chatId).collection("messages").document()
    val message = Message(
        id = newDoc.id,
        tempId = messageContent.tempId,
        content = messageContent.content,
        timestamp = System.currentTimeMillis(),
        fromUser = id.uid
    )

    newDoc.set(message).await()

    call.respond(message.toMessageDTO())
}
