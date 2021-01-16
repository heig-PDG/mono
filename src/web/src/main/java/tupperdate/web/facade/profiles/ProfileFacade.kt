package tupperdate.web.facade.profiles

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface ProfileFacade {

    /**
     * Create a new user's profile
     */
    suspend fun save(
        user: User,
        profileId: String,
        profile: NewProfile,
    ): Result<Unit>

    /**
     * Fetch and return profile of an user
     */
    suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile>

    /**
     * Register the login of an user, generate it a token
     */
    suspend fun register(
        user: User,
        token: NewNotificationToken,
    ): Result<Unit>
}
