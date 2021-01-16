package tupperdate.web.model.profiles

import tupperdate.web.model.Result

interface UserRepository {
    /**
     * Create and register a new user
     */
    suspend fun save(
        user: ModelNewUser,
    ): Result<Unit>

    /**
     * Update (patch) a user
     */
    suspend fun update(
        user: ModelPartUser,
    ): Result<Unit>

    /**
     * Update id of the last recipe seen by an user
     */
    suspend fun updateLastSeenRecipe(
        user: User,
        lastSeenRecipe: Long,
    ): Result<Unit>

    /**
     * Read user's information
     */
    suspend fun read(
        user: User,
    ): Result<ModelUser>

    /**
     * Register a new user
     */
    suspend fun register(
        user: User,
        token: ModelNotificationToken,
    ): Result<Unit>
}
