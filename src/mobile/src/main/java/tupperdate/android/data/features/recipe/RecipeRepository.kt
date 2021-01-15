package tupperdate.android.data.features.recipe

import kotlinx.coroutines.flow.Flow

/**
 * An interface describing possible interactions with the recipes. Implementation of the repository
 * will automatically manage networking and data management for seamless synchronisation.
 */
interface RecipeRepository {

    // READING

    /**
     * Returns the information of a single [Recipe] in the database.
     */
    fun single(id: String): Flow<Recipe>

    /**
     * Returns all the [Recipe] that should be displayed in the stack.
     */
    fun stack(): Flow<List<Recipe>>

    /**
     * Returns all the [Recipe] that have been created by the currently logged in user.
     */
    fun own(): Flow<List<Recipe>>

    /**
     * Creates a new recipe, provided with a [NewRecipe] instance.
     */
    suspend fun create(recipe: NewRecipe)

    // WRITING

    /**
     * Likes a recipe, based on its id.
     */
    suspend fun like(id: String)

    /**
     * Dislikes a recipe, based on its id.
     */
    suspend fun dislike(id: String)
}
