package tupperdate.android.data.features.recipe.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi

@Dao
@InternalDataApi
abstract class RecipeDao {

    // TODO : Make sure we have not voted on the retrieved recipes.
    @Query("SELECT * FROM recipes ORDER BY timestamp ASC")
    abstract fun findAll(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    abstract fun find(id: String): Flow<RecipeEntity>

    @Insert(onConflict = REPLACE)
    abstract suspend fun insert(entity: RecipeEntity): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertAll(entities: Collection<RecipeEntity>)

    @Update(onConflict = REPLACE)
    abstract suspend fun update(entity: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id = :id")
    abstract suspend fun delete(id: String): Int

    @Query("DELETE FROM recipes")
    abstract suspend fun deleteAll(): Int
}
