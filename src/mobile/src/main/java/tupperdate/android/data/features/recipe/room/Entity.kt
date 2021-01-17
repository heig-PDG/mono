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
    // Recipe properties.
    @ColumnInfo(name = "owner") val ownerId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "picture") val picture: String?,
    // Recipe attributes.
    @ColumnInfo(name = "attributeVegetarian") val attributeVegetarian: Boolean,
    @ColumnInfo(name = "attributeWarm") val attributeWarm: Boolean,
    @ColumnInfo(name = "attributeHasAllergens") val attributeHasAllergens: Boolean,
    // Client-only state.
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val localId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture") val picture: String?,
    @ColumnInfo(name = "isWarm") val warm: Boolean,
    @ColumnInfo(name = "isVegetarian") val vegetarian: Boolean,
    @ColumnInfo(name = "hasAllergens") val allergens: Boolean,
)

@Entity(tableName = "recipesUpdates")
@InternalDataApi
data class PendingUpdateRecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "identifier") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "titleUpdate") val titleUpdate: Boolean,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "descriptionUpdate") val descriptionUpdate: Boolean,
    @ColumnInfo(name = "picture") val picture: String?,
    @ColumnInfo(name = "pictureUpdate") val pictureUpdate: Boolean,
    @ColumnInfo(name = "isWarm") val warm: Boolean,
    @ColumnInfo(name = "isWarmUpdate") val warmUpdate: Boolean,
    @ColumnInfo(name = "isVegetarian") val vegetarian: Boolean,
    @ColumnInfo(name = "isVegetarianUpdate") val vegetarianUpdate: Boolean,
    @ColumnInfo(name = "hasAllergens") val allergens: Boolean,
    @ColumnInfo(name = "hasAllergensUpdate") val allergensUpdate: Boolean,
)
