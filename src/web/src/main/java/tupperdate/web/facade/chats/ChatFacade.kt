package tupperdate.web.facade.chats

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ChatFacade {

    suspend fun readAll(
        user: User,
    ): Result<List<Chat>>

    suspend fun readOne(
        user: User,
    ): Result<Chat>

    suspend fun readMessages(
        user: User,
        otherUserId: String,
    ): Result<List<Chat>>

    suspend fun sendMessage(
        user: User,
        otherUserId: String,
        content: String,
    ): Result<Unit>
}
