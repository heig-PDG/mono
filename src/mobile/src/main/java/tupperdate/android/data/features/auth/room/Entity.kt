package tupperdate.android.data.features.auth.room

import androidx.room.*
import tupperdate.android.data.InternalDataApi

@TypeConverters(ProfileEntityConverters::class)
@Entity(tableName = "profiles")
@InternalDataApi
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "status") val status: ProfileEntityStatus,
    @ColumnInfo(name = "name") val displayName: String? = null,
    @ColumnInfo(name = "picture") val profilePicture: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,
)

enum class ProfileEntityStatus {
    Loading,
    AbsentProfile,
    CompleteProfile,
}

class ProfileEntityConverters {

    @TypeConverter
    fun toStatus(value: String) = enumValueOf<ProfileEntityStatus>(value)

    @TypeConverter
    fun fromStatus(value: ProfileEntityStatus) = value.name
}
