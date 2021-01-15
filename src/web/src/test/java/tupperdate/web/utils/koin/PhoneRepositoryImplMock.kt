package tupperdate.web.utils.koin

import tupperdate.web.facade.accounts.Phone
import tupperdate.web.model.Result
import tupperdate.web.model.accounts.PhoneRepository
import tupperdate.web.model.profiles.User

class PhoneRepositoryImplMock() : PhoneRepository {
    override suspend fun read(user: User): Result<Phone> {
        return Result.Ok(Phone("+41 12 345 67 89"))
    }

}
