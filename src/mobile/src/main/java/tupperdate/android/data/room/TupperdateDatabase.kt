package tupperdate.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.room.ProfileDao
import tupperdate.android.data.features.auth.room.ProfileEntity
import tupperdate.android.data.features.recipe.room.PendingNewRecipeEntity
import tupperdate.android.data.features.recipe.room.PendingRateRecipeEntity
import tupperdate.android.data.features.recipe.room.RecipeDao
import tupperdate.android.data.features.recipe.room.RecipeEntity

@Database(
    entities = [
        ProfileEntity::class,
        RecipeEntity::class,
        // Changes to be sent to the server.
        PendingNewRecipeEntity::class,
        PendingRateRecipeEntity::class,
    ],
    version = 1,
    exportSchema = false, // we're not yet interested in schema migrations
)
@InternalDataApi
abstract class TupperdateDatabase : RoomDatabase() {
    abstract fun profiles(): ProfileDao
    abstract fun recipes(): RecipeDao
}
