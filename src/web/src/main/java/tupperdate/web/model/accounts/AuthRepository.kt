package tupperdate.web.model.accounts

import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.accounts.Token
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface AuthRepository {
    /**
     * Read a token and return associated account
     */
    suspend fun read(
        token: Token,
    ): Result<AccountId>

    /**
     * Revoke tokens refreshed
     */
    suspend fun revokeRefreshTokens(
        user: User,
    ): Result<Unit>
}
