package tupperdate.web.model.accounts.firestore

import com.google.firebase.auth.FirebaseAuth
import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.accounts.Token
import tupperdate.web.model.Result
import tupperdate.web.model.accounts.AuthRepository
import tupperdate.web.model.profiles.User

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
) : AuthRepository {
    override suspend fun read(token: Token): Result<AccountId> =
        try {
            val checked = auth.verifyIdToken(token.bearer, true)
            Result.Ok(AccountId(checked.uid))
        } catch (throwable: Throwable) {
            Result.Unauthorized()
        }

    override suspend fun revokeRefreshTokens(user: User): Result<Unit> =
        try {
            auth.revokeRefreshTokens(user.id.uid)
            Result.Ok(Unit)
        } catch (throwable: Throwable) {
            Result.NotFound()
        }
}
