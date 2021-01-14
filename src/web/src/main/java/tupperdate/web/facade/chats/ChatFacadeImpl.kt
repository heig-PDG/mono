package tupperdate.web.facade.chats

import tupperdate.web.model.Result
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.profiles.User

class ChatFacadeImpl(
    private val chats: ChatRepository,
) : ChatFacade {

    override suspend fun readAll(
        user: User,
    ): Result<List<Chat>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(
        user: User,
    ): Result<Chat> {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(
        user: User,
        otherUserId: String,
    ): Result<List<Chat>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        user: User,
        otherUserId: String,
        content: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

}
