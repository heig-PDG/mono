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
abstract class RecipeDao {

    // RECIPES ACCESS

    /**
     * Finds all the recipes that should be displayed in the recipe stack. If a recipe was already
     * liked or disliked, it should not be inserted.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE recipes.inStack = 1 AND recipes.owner != :user
        ORDER BY timestamp ASC
        """
    )
    abstract fun recipesStack(user: String): Flow<List<RecipeEntity>>

    /**
     * Finds all the recipe that should be displayed for the specified user.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE recipes.owner = :user
        ORDER BY recipes.timestamp ASC
        """
    )
    abstract fun recipesForUser(user: String): Flow<List<RecipeEntity>>

    /**
     * Finds the recipe for a certain provided id.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE id = :forId
        """
    )
    abstract fun recipe(forId: String): Flow<RecipeEntity?>

    /**
     * Inserts a single [RecipeEntity], replacing any recipe with the same content locally.
     *
     * @see [recipesInsertAll] a variant that handles upserting gracefully.
     */
    @Insert(onConflict = REPLACE)
    abstract suspend fun recipesInsert(entity: RecipeEntity): Long

    /**
     * Un-swipes a single [RecipeEntity], adding it back to the stack. It will not necessarily
     * be added to the top of the stack.
     */
    @Transaction
    open suspend fun recipesUnswipe(forId: String) {
        val recipe = recipeOnce(forId) ?: return
        recipesInsert(recipe.copy(inStack = true))
    }

    /**
     * Similar to [recipe], but does not return a [Flow] with changes over time. You're very
     * unlikely to use this method.
     *
     * It remains here to support the [recipesInsertAll] method.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE id = :forId
        """
    )
    @Deprecated("Prefer using recipe()")
    abstract suspend fun recipeOnce(forId: String): RecipeEntity?

    /**
     * Inserts a [Collection] of [RecipeEntity], respecting client-side likes and dislikes. This
     * makes sure that swiped cards (which are not in the stack anymore) do not get "added" in the
     * stack again if the like / dislike has not been synced yet, but some recipes are updated from
     * the server.
     */
    @Transaction
    open suspend fun recipesInsertAll(entities: Collection<RecipeEntity>) {
        for (recipe in entities) {
            // We can't use recipe(recipe.identifier).firstOrNull() because it deadlocks. This is
            // probably due to coroutine contexts differing between the invocations, resulting in
            // what looks like two exclusive transactional calls to Room.
            val existing = recipeOnce(recipe.identifier)
            if (existing != null) {
                recipesInsert(recipe.copy(inStack = existing.inStack != false))
            } else {
                recipesInsert(recipe)
            }
        }
    }

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

    @Query("SELECT * FROM recipesRatings")
    abstract fun pendingRatings(): Flow<List<PendingRateRecipeEntity>>

    @Query("DELETE FROM recipesRatings WHERE recipesRatings.id = :id")
    abstract suspend fun pendingRatingsDelete(id: String)

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

    // CREATIONS

    @Query("SELECT * FROM recipesCreations")
    abstract fun pendingCreations(): Flow<List<PendingNewRecipeEntity>>

    @Insert
    abstract suspend fun pendingRecipesInsert(dto: PendingNewRecipeEntity)

    @Query("DELETE FROM recipesCreations WHERE recipesCreations.id = :localId")
    abstract suspend fun pendingCreationsDelete(localId: Long)

    // UPDATES
    @Query("SELECT * FROM recipesUpdates")
    abstract fun pendingUpdates(): Flow<List<PendingUpdateRecipeEntity>>

    @Insert
    abstract suspend fun pendingUpdatesRecipesInsert(dto: PendingUpdateRecipeEntity)

    @Query("DELETE FROM recipesUpdates WHERE recipesUpdates.identifier = :id")
    abstract suspend fun pendingUpdatesDelete(id: String)
}
