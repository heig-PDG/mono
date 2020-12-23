package tupperdate.android.data.features.recipe.room

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tupperdate.android.data.features.recipe.Recipe

@Entity(tableName = "recipes")
@Immutable
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "picture") val picture: String,
)

fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        identifier = this.identifier,
        title = this.title,
        description = this.description,
        timestamp = this.timestamp,
        picture = this.picture,
    )
}
