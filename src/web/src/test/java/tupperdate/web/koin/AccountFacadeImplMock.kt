package tupperdate.web.koin

import tupperdate.web.facade.accounts.AccountFacade
import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.accounts.Token
import tupperdate.web.model.Result

class AccountFacadeImplMock() : AccountFacade {

    override suspend fun authenticate(bearer: Token): Result<AccountId> {
        return Result.Ok(AccountId("botId"))
    }
}
