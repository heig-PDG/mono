package tupperdate.android.data.features.recipe.room

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tupperdate.android.data.InternalDataApi

@Entity(tableName = "recipes")
@Immutable
@InternalDataApi
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "inStack", defaultValue = "NULL") val inStack: Boolean?,
)

@Entity(
    tableName = "recipesRatings",
)
@InternalDataApi
data class PendingRateRecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "pendingLike") val like: Boolean,
    @ColumnInfo(name = "pendingDislike") val dislike: Boolean,
)

@Entity(tableName = "recipesCreations")
@InternalDataApi
data class PendingNewRecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val identifier: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "isWarm") val warm: Boolean,
    @ColumnInfo(name = "isVegetarian") val vegetarian: Boolean,
    @ColumnInfo(name = "hasAllergens") val allergens: Boolean,
)
