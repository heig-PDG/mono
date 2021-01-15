package tupperdate.web.facade.recipes

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User
import tupperdate.web.model.recipes.RecipeRepository
import tupperdate.web.model.recipes.toModelNewRecipe

class RecipeFacadeImpl(
    private val recipes: RecipeRepository,
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
        TODO("Not yet implemented")
    }

    override suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<Recipe>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<Recipe> {
        TODO("Not yet implemented")
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
