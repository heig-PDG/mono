package tupperdate.web.model.accounts.firestore

import com.google.firebase.auth.FirebaseAuth
import tupperdate.web.facade.accounts.Phone
import tupperdate.web.legacy.util.await
import tupperdate.web.model.Result
import tupperdate.web.model.accounts.PhoneRepository
import tupperdate.web.model.profiles.User

class FirebasePhoneRepository(
    private val auth: FirebaseAuth,
) : PhoneRepository {
    override suspend fun read(user: User): Result<Phone> =
        try {
            val number = auth.getUserAsync(user.id.uid).await().phoneNumber
            Result.Ok(Phone(number))
        } catch (throwable: Throwable) {
            Result.NotFound()
        }
}
