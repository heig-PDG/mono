package tupperdate.android.data.features.recipe

import kotlinx.coroutines.flow.Flow

/**
 * An interface describing possible interactions with the recipes. Implementation of the repository
 * will automatically manage networking and data management for seamless synchronisation.
 */
interface RecipeRepository {

    /**
     * Creates a new recipe, provided with a [NewRecipe] instance.
     */
    fun create(recipe: NewRecipe)

    /**
     * Returns the information of a single [Recipe] in the database.
     */
    fun single(id: String): Flow<Recipe>

    /**
     * Returns all the [Recipe] that should be displayed in the stack.
     */
    fun stack(): Flow<List<Recipe>>

    /**
     * Likes a recipe, based on its id.
     */
    fun like(id: String)

    /**
     * Dislikes a recipe, based on its id.
     */
    fun dislike(id: String)
}
