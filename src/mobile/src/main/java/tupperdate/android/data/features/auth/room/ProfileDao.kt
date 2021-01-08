package tupperdate.android.data.features.auth.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi

@Dao
@InternalDataApi
abstract class ProfileDao {

    @Query("SELECT * FROM profiles")
    abstract fun all(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE profiles.uid = :uid")
    abstract fun forUid(uid: String): Flow<ProfileEntity?>

    @Query("DELETE FROM profiles WHERE uid = :uid")
    abstract suspend fun delete(uid: String)

    @Query("DELETE FROM profiles")
    abstract suspend fun deleteAll()

    @Insert
    abstract suspend fun save(profile: ProfileEntity)
}
