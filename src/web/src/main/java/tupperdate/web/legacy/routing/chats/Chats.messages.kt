package tupperdate.web.legacy.routing.chats

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.legacy.auth.tupperdateAuthPrincipal
import tupperdate.web.legacy.exceptions.statusException
import tupperdate.web.legacy.model.Chat
import tupperdate.web.legacy.model.Message
import tupperdate.web.legacy.model.toMessageDTO
import tupperdate.web.legacy.util.await

fun Route.getMessages(store: Firestore) = get("/{userId}/messages") {
    val id = call.tupperdateAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    val userId = call.parameters["userId"] ?: statusException(HttpStatusCode.BadRequest)

    val chatId = minOf(id.uid, userId) + "_" + maxOf(id.uid, userId)
    val chat = store.collection("chats").document(chatId).get().await().toObject(Chat::class.java)
        ?: statusException(HttpStatusCode.NotFound)

    if (chat.user1Recipes == null || chat.user2Recipes == null) {
        statusException(HttpStatusCode.NotFound)
    }

    val messages = store.collection("chats").document(chatId).collection("messages")
        .orderBy("timestamp", Query.Direction.DESCENDING).get().await()
        .toObjects(Message::class.java)

    val messageDTOs = messages.map { it.toMessageDTO() }

    call.respond(messageDTOs)
}
