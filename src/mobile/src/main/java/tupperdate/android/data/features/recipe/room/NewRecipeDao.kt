package tupperdate.android.data.features.recipe.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NewRecipeDao {

    @Query("SELECT * FROM new_recipes")
    abstract fun all(): Flow<List<NewRecipeEntity>>

    @Insert(onConflict = REPLACE)
    abstract fun insert(recipe: NewRecipeEntity)

    @Query("DELETE FROM new_recipes WHERE id = :id")
    abstract fun delete(id: Long)
}
