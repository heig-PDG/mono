package tupperdate.web.facade.recipes

import tupperdate.web.model.Result
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository
import tupperdate.web.model.recipes.RecipeRepository
import tupperdate.web.model.recipes.toModelNewRecipe
import tupperdate.web.model.recipes.toRecipe

class RecipeFacadeImpl(
    private val recipes: RecipeRepository,
    private val users: UserRepository,
) : RecipeFacade {

    override suspend fun save(
        user: User,
        recipe: NewRecipe,
    ): Result<Unit> {
        return recipes.save(user, recipe.toModelNewRecipe())
    }

    override suspend fun readOwn(
        user: User,
    ): Result<List<Recipe>> {
        return recipes.readOwn(user).map { it.map { that -> that.toRecipe() } }
    }

    override suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<Recipe>> {
        val lastSeenRecipe = when(val time = users.read(user).map { it.lastSeenRecipe }) {
            is Result.Ok -> time.result
            else -> 0
        }

        return recipes.readAll(user, count, lastSeenRecipe).map { it.map { that -> that.toRecipe() } }
    }

    override suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<Recipe> {
        return recipes.readOne(user, recipeId).map { it.toRecipe() }
    }

    override suspend fun like(
        user: User,
        recipeId: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun dislike(
        user: User,
        recipeId: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

}
