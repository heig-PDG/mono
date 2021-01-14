package tupperdate.web.model.chats

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ChatRepository {

    suspend fun readAll(
        user: User,
    ): Result<List<ModelChat>>

    suspend fun readOne(
        user: User,
        otherUserId: String,
    ): Result<ModelChat>

    suspend fun readMessages(
        user: User,
        otherUserId: String,
    ): Result<List<ModelMessage>>

    suspend fun sendMessage(
        user: User,
        otherUserId: String,
        content: String,
    ): Result<Unit>

}
