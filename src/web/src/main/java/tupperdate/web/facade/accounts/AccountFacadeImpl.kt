package tupperdate.web.facade.accounts

import tupperdate.web.model.Result
import tupperdate.web.model.accounts.AuthRepository
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User

class AccountFacadeImpl(
    private val auth: AuthRepository,
) : AccountFacade {

    override suspend fun authenticate(bearer: Token): Result<AccountId> {
        return auth.read(bearer).map { AccountId(it.uid) }
    }

    override suspend fun logout(user: User): Result<Unit> {
        return auth.revokeRefreshTokens(user)
    }
}
