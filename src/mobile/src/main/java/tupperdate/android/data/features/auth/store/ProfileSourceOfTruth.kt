package tupperdate.android.data.features.auth.store

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.AuthenticationStatus.*
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.auth.room.ProfileDao
import tupperdate.android.data.features.auth.room.ProfileEntity
import tupperdate.android.data.features.auth.room.ProfileEntityStatus

@InternalDataApi
class ProfileSourceOfTruth(
    private val dao: ProfileDao,
) : SourceOfTruth<FirebaseUid, AuthenticationStatus, AuthenticationStatus> {

    override suspend fun delete(key: FirebaseUid) = dao.delete(key)

    override suspend fun deleteAll() = dao.deleteAll()

    override fun reader(key: FirebaseUid): Flow<AuthenticationStatus> = dao.forUid(key)
        .map {
            when (it?.status) {
                ProfileEntityStatus.Loading -> LoadingProfile(key)
                ProfileEntityStatus.AbsentProfile -> AbsentProfile(key)
                ProfileEntityStatus.CompleteProfile -> CompleteProfile(
                    identifier = key,
                    displayName = it.displayName!!,
                    displayPictureUrl = it.profilePicture,
                    phoneNumber = it.phone!!,
                )
                null -> Unknown
            }
        }

    override suspend fun write(
        key: FirebaseUid,
        value: AuthenticationStatus,
    ) {
        // TODO : Only allow profile status upgrades.
        when (value) {
            Unknown, None -> Unit // Skipped.
            is LoadingProfile -> dao.save(
                ProfileEntity(
                    uid = key,
                    status = ProfileEntityStatus.Loading,
                )
            )
            is AbsentProfile -> dao.save(
                ProfileEntity(
                    uid = key,
                    status = ProfileEntityStatus.AbsentProfile,
                )
            )
            is CompleteProfile -> dao.save(
                ProfileEntity(
                    uid = value.identifier,
                    status = ProfileEntityStatus.CompleteProfile,
                    displayName = value.displayName,
                    profilePicture = value.displayPictureUrl,
                    phone = value.phoneNumber,
                )
            )
        }
    }
}
