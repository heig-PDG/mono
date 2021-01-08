package tupperdate.android.data.features.auth.room

import com.dropbox.android.external.store4.SourceOfTruth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.common.dto.UserDTO

@InternalDataApi
class ProfileSourceOfTruth(
    private val dao: ProfileDao,
) : SourceOfTruth<String, Pair<FirebaseUser, UserDTO>, AuthenticationStatus.CompleteProfile> {

    override suspend fun delete(key: String) = dao.delete(key)

    override suspend fun deleteAll() = dao.deleteAll()

    override fun reader(key: String) = dao.forUid(key).map {
        it?.let { entity ->
            AuthenticationStatus.CompleteProfile(
                identifier = entity.uid,
                displayName = entity.displayName,
                displayPictureUrl = entity.profilePicture,
                phoneNumber = entity.phone,
            )
        }
    }


    override suspend fun write(key: String, value: Pair<FirebaseUser, UserDTO>) {
        // TODO : Eventually pass the token when writing the profile rather than fetching it here.
        val (firebase, dto) = value
        val profile = ProfileEntity(
            uid = firebase.uid,
            displayName = dto.displayName ?: "",
            profilePicture = dto.picture ?: "",
            phone = firebase.phoneNumber ?: "",
        )
        dao.save(profile)
    }
}
