package tupperdate.web.facade.accounts

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

inline class Token(val bearer: String)
inline class AccountId(val uid: String)
inline class Phone(val number: String)

interface AccountFacade {
    /**
     * Validate an user's token and record it as authenticated
     */
    suspend fun authenticate(bearer: Token): Result<AccountId>

    /**
     * Revoke an user's token resulting a logout
     */
    suspend fun logout(user: User): Result<Unit>
}
