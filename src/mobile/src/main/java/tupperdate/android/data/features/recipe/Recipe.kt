package tupperdate.android.data.features.recipe

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
@Immutable
data class Recipe(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "picture") val picture: String,
)
