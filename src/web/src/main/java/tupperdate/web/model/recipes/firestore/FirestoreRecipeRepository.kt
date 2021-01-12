package tupperdate.web.model.recipes.firestore

import com.google.cloud.firestore.Firestore
import com.google.cloud.storage.Storage
import tupperdate.web.facade.PictureBase64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User
import tupperdate.web.model.recipes.ModelNewRecipe
import tupperdate.web.model.recipes.ModelRecipe
import tupperdate.web.model.recipes.RecipeRepository

class FirestoreRecipeRepository(
    private val store: Firestore,
    private val storage: Storage,
) : RecipeRepository {

    override suspend fun save(
        user: User,
        recipe: ModelNewRecipe<PictureBase64>,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun readOwn(
        user: User,
    ): Result<List<ModelRecipe<PictureUrl>>> {
        TODO("Not yet implemented")
    }

    override suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<ModelRecipe<PictureUrl>>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<ModelRecipe<PictureUrl>> {
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
