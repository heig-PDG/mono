package tupperdate.web.model.chats.firestore

import org.koin.dsl.module
import tupperdate.web.model.chats.ChatRepository

val KoinModuleModelChatsFirestore = module {
    factory<ChatRepository> { FirestoreChatRepository(get()) }
}
