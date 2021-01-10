package tupperdate.android.data.features.recipe.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi

@Dao
@InternalDataApi
abstract class Dao {

    // RECIPES ACCESS

    /**
     * Finds all the recipes that should be displayed in the recipe stack. If a recipe was already
     * liked or disliked, it should not be inserted.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE recipes.inStack = 1
        ORDER BY timestamp ASC
        """
    )
    abstract fun recipesStack(): Flow<List<RecipeEntity>>

    /**
     * Finds the recipe for a certain provided id.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE id = :forId
        """
    )
    abstract fun recipe(forId: String): Flow<RecipeEntity>

    @Insert(onConflict = REPLACE)
    // TODO : Handle upserting better ? Keep current on NULL.
    abstract suspend fun recipesInsert(entity: RecipeEntity): Long

    // TODO : Handle inStack better.
    @Insert(onConflict = REPLACE)
    // TODO : Handle upserting better ? Keep existing on NULL.
    abstract suspend fun recipesInsertAll(entities: Collection<RecipeEntity>)

    /**
     * Removes the recipe with the provided identifier from the local cache.
     */
    @Query("DELETE FROM recipes WHERE id = :id")
    abstract suspend fun recipesDelete(id: String): Int

    /**
     * Removes all the recipes from the local cache.
     */
    @Query("DELETE FROM recipes")
    abstract suspend fun recipesDeleteAll(): Int

    // VOTES

    /**
     * Marks the recipe with the provided id as voted, and removes it from the recipes in the stack.
     * This should be used in conjunction with adding a pending like / dislike to the appropriate
     * entity.
     */
    @Query(
        """
        UPDATE recipes 
        SET inStack = 0
        WHERE recipes.id = :id
        """
    )
    abstract suspend fun recipesUnstack(id: String)

    /**
     * Inserts a pending rating request. Pending requests will be conflated, such that only the
     * latest user-issued commands are sent.
     *
     * @param id the id of the recipe to like or dislike
     * @param like `true` iff the recipe should be liked
     * @param dislike `true` iff the recipe should be disliked
     */
    @Query(
        """
        REPLACE INTO recipesRatings(id, pendingLike, pendingDislike)
        VALUES (:id, :like, :dislike)
        """
    )
    abstract suspend fun pendingRatingsInsert(id: String, like: Boolean, dislike: Boolean)

    /**
     * Likes a recipe, given its id.
     */
    @Transaction
    open suspend fun recipesLike(id: String) {
        pendingRatingsInsert(id, like = true, dislike = false)
        recipesUnstack(id)
    }

    /**
     * Dislikes a recipe, given its id.
     */
    @Transaction
    open suspend fun recipesDislike(id: String) {
        pendingRatingsInsert(id, like = false, dislike = true)
        recipesUnstack(id)
    }
}
