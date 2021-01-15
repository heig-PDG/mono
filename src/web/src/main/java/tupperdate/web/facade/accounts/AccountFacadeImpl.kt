package tupperdate.web.facade.accounts

import tupperdate.web.model.Result
import tupperdate.web.model.accounts.AuthRepository
import tupperdate.web.model.map

class AccountFacadeImpl(
    private val auth: AuthRepository,
) : AccountFacade {

    override suspend fun authenticate(bearer: Token): Result<AccountId> {
        return auth.read(bearer).map { AccountId(it.uid) }
    }
}
