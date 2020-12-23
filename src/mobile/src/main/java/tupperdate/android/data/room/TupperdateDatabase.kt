package tupperdate.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tupperdate.android.data.features.recipe.room.NewRecipeDao
import tupperdate.android.data.features.recipe.room.NewRecipeEntity
import tupperdate.android.data.features.recipe.room.RecipeDao
import tupperdate.android.data.features.recipe.room.RecipeEntity

@Database(
    entities = [
        RecipeEntity::class,
        NewRecipeEntity::class,
    ],
    version = 1,
    exportSchema = false, // we're not yet interested in schema migrations
)
abstract class TupperdateDatabase : RoomDatabase() {
    abstract fun recipes(): RecipeDao
    abstract fun newRecipes(): NewRecipeDao
}
