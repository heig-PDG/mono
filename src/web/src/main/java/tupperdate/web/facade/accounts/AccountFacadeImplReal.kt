package tupperdate.web.facade.accounts

import tupperdate.web.model.Result
import tupperdate.web.model.accounts.AuthRepository
import tupperdate.web.model.accounts.PhoneRepository
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User

class AccountFacadeImplReal(
    private val auth: AuthRepository,
    private val phones: PhoneRepository,
) : AccountFacade {

    override suspend fun authenticate(bearer: Token): Result<AccountId> {
        return auth.read(bearer).map { AccountId(it.uid) }
    }

    override suspend fun phone(uuid: AccountId): Result<Phone> {
        return phones.read(User(id = uuid)).map { Phone(it.number) }
    }
}
