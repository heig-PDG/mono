package tupperdate.web.model.accounts

import tupperdate.web.facade.accounts.Phone
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface PhoneRepository {
    /**
     * Return user's phone number
     */
    suspend fun read(
        user: User,
    ): Result<Phone>
}
