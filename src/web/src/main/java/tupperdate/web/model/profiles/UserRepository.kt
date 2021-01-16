package tupperdate.web.model.profiles

import tupperdate.web.model.Result

interface UserRepository {
    suspend fun save(
        user: ModelNewUser,
    ): Result<Unit>

    suspend fun updateLastSeenRecipe(
        user: User,
        lastSeenRecipe: Long,
    ): Result<Unit>

    suspend fun read(
        user: User,
    ): Result<ModelUser>

    suspend fun register(
        user: User,
        token: ModelNotificationToken,
    ): Result<Unit>
}
