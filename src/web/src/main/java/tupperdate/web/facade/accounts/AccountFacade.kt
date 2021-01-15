package tupperdate.web.facade.accounts

import tupperdate.web.model.Result

inline class Token(val bearer: String)
inline class AccountId(val uid: String)
inline class Phone(val number: String)

interface AccountFacade {
    suspend fun authenticate(bearer: Token): Result<AccountId>
}
