package tupperdate.android.data.features.auth.store

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.room.ProfileDao
import tupperdate.android.data.features.auth.room.ProfileEntity

@InternalDataApi
class ProfileSourceOfTruth(
    private val dao: ProfileDao,
) : SourceOfTruth<Pair<String, String>, AuthenticationStatus.CompleteProfile, AuthenticationStatus.CompleteProfile> {

    override suspend fun delete(key: Pair<String, String>) = dao.delete(key.first)

    override suspend fun deleteAll() = dao.deleteAll()

    override fun reader(key: Pair<String, String>) = dao.forUid(key.first)
        .filterNotNull()
        .map {
            it.let { entity ->
                AuthenticationStatus.CompleteProfile(
                    identifier = entity.uid,
                    displayName = entity.displayName,
                    displayPictureUrl = entity.profilePicture,
                    phoneNumber = entity.phone,
                )
            }
        }

    override suspend fun write(
        key: Pair<String, String>,
        value: AuthenticationStatus.CompleteProfile,
    ) {
        dao.save(
            ProfileEntity(
                uid = value.identifier,
                displayName = value.displayName,
                profilePicture = value.displayPictureUrl,
                phone = value.phoneNumber,
            )
        )
    }
}
