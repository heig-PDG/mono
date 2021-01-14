package tupperdate.web.facade.recipes

import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface RecipeFacade {

    suspend fun save(
        user: User,
        recipe: NewRecipe<PictureBase64>,
    ): Result<Unit>

    suspend fun readOwn(
        user: User,
    ): Result<List<Recipe<PictureUrl>>>

    suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<Recipe<PictureUrl>>>

    suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<Recipe<PictureUrl>>

    suspend fun like(
        user: User,
        recipeId: String,
    ): Result<Unit>

    suspend fun dislike(
        user: User,
        recipeId: String,
    ): Result<Unit>
}
