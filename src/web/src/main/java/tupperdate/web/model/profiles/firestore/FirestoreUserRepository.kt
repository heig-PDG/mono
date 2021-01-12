package tupperdate.web.model.profiles.firestore

import com.google.cloud.firestore.Firestore
import com.google.cloud.storage.Storage
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.ModelUser
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository

class FirestoreUserRepository(
    private val store: Firestore,
    private val storage: Storage,
) : UserRepository {

    override suspend fun save(
        user: ModelUser,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun read(
        user: User,
    ): Result<ModelUser> {
        TODO("Not yet implemented")
    }
}
