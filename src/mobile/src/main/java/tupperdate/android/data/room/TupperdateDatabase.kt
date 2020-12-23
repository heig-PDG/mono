package tupperdate.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.room.RecipeDao
import tupperdate.android.data.features.recipe.room.RecipeEntity

@Database(
    entities = [
        RecipeEntity::class,
    ],
    version = 1,
    exportSchema = false, // we're not yet interested in schema migrations
)
@InternalDataApi
abstract class TupperdateDatabase : RoomDatabase() {
    abstract fun recipes(): RecipeDao
}
