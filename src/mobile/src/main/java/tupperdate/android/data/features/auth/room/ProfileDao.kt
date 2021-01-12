package tupperdate.android.data.features.auth.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi

@Dao
@InternalDataApi
abstract class ProfileDao {

    @Query("SELECT * FROM profiles")
    abstract fun all(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE profiles.uid = :uid")
    abstract fun forUid(uid: String): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE profiles.uid = :uid")
    abstract suspend fun forUidOnce(uid: String): ProfileEntity?

    @Query("DELETE FROM profiles WHERE uid = :uid")
    abstract suspend fun delete(uid: String)

    @Query("DELETE FROM profiles")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replace(profile: ProfileEntity)

    @Transaction
    open suspend fun save(profile: ProfileEntity) {
        val existing = forUidOnce(profile.uid)
        if (existing == null || profile.status >= existing.status) {
            replace(profile)
        }
    }
}
