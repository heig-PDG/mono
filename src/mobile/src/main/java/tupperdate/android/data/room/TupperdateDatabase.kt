package tupperdate.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tupperdate.android.data.Recipe
import tupperdate.android.data.room.recipe.RecipeDao

@Database(
    entities = [
        Recipe::class,
    ],
    version = 1,
    exportSchema = false, // we're not yet interested in schema migrations
)
abstract class TupperdateDatabase : RoomDatabase() {
    abstract fun recipes(): RecipeDao
}
