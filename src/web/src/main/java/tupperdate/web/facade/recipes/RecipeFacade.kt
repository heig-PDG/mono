package tupperdate.web.facade.recipes

import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface RecipeFacade {
    /**
     * Save a new recipe
     */
    suspend fun save(
        user: User,
        recipe: NewRecipe,
    ): Result<Unit>

    /**
     * Update a recipe with optional values
     */
    suspend fun update(
        user: User,
        recipe: PartRecipe,
        recipeId: String,
    ): Result<Unit>

    /**
     * Read and return all user's own recipes
     */
    suspend fun readOwn(
        user: User,
    ): Result<List<Recipe>>

    /**
     * Read all recipes
     */
    suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<Recipe>>

    /**
     * Read one recipe
     */
    suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<Recipe>

    /**
     * Like one recipe
     */
    suspend fun like(
        user: User,
        recipeId: String,
    ): Result<Unit>

    /**
     * Dislike one recipe
     */
    suspend fun dislike(
        user: User,
        recipeId: String,
    ): Result<Unit>
}
