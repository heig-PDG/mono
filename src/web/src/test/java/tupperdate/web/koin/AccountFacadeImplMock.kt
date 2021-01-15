package tupperdate.web.koin

import tupperdate.web.facade.accounts.AccountFacade
import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.accounts.Phone
import tupperdate.web.facade.accounts.Token
import tupperdate.web.model.Result

class AccountFacadeImplMock() : AccountFacade {

    override suspend fun authenticate(bearer: Token): Result<AccountId> {
        return Result.Ok(AccountId("uniqueId"))
    }

    override suspend fun phone(uuid: AccountId): Result<Phone> {
        return Result.Ok(Phone(number = "+41 12 345 67 89"))
    }
}
