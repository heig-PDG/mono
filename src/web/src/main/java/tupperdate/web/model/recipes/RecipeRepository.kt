package tupperdate.web.model.recipes

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface RecipeRepository {

    /**
     * Save a new recipe
     */
    suspend fun save(
        user: User,
        recipe: ModelNewRecipe,
    ): Result<Unit>

    /**
     * Update a recipe
     */
    suspend fun update(
        user: User,
        partRecipe: ModelPartRecipe,
    ): Result<Unit>

    /**
     * Read of user's recipes
     */
    suspend fun readOwn(
        user: User,
    ): Result<List<ModelRecipe>>

    /**
     * Read all liked recipes
     */
    suspend fun readAll(
        user: User,
        count: Int,
        lastSeenRecipe: Long,
    ): Result<List<ModelRecipe>>

    /**
     * Read one liked recipe
     */
    suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<ModelRecipe>
}
