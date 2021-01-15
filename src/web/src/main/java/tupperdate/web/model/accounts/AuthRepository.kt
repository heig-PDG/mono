package tupperdate.web.model.accounts

import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.accounts.Token
import tupperdate.web.model.Result

interface AuthRepository {
    suspend fun read(
        token: Token,
    ): Result<AccountId>
}
