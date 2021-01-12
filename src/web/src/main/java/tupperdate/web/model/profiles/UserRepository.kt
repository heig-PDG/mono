package tupperdate.web.model.profiles

import tupperdate.web.model.Result

interface UserRepository {
    suspend fun save(user: ModelUser): Result<Unit>
    suspend fun read(user: User): Result<ModelUser>
}
