@file:JvmName("NewRecipeEntity")

package tupperdate.android.data.features.recipe.room

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_recipes")
@Immutable
data class NewRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val identifier: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "vegetarian") val isVegetarian: Boolean,
    @ColumnInfo(name = "isWarm") val isWarm: Boolean,
    @ColumnInfo(name = "allergens") val hasAllergens: Boolean,
)
