package tupperdate.android.data.room.recipe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.Recipe

@Dao
abstract class RecipeDao {

    // TODO : Make sure we have not voted on the retrieved recipes.
    @Query("SELECT * FROM recipes ORDER BY timestamp ASC")
    abstract fun findAll(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    abstract fun find(id: String): Flow<Recipe>

    @Insert(onConflict = REPLACE)
    abstract suspend fun insert(entity: Recipe): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertAll(entities: Collection<Recipe>)

    @Update(onConflict = REPLACE)
    abstract suspend fun update(entity: Recipe)

    @Query("DELETE FROM recipes WHERE id = :id")
    abstract suspend fun delete(id: String): Int

    @Query("DELETE FROM recipes")
    abstract suspend fun deleteAll(): Int
}
