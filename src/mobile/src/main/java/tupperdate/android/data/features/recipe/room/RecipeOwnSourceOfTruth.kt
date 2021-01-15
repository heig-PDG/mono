package tupperdate.android.data.features.recipe.room

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * An implementation of a [SourceOfTruth] that can manage all the recipes for the currently logged
 * in user. It does not require a specific key to be fetched.
 *
 * @param dao the [RecipeDao] that is accessed by this [SourceOfTruth].
 */
@InternalDataApi
class RecipeOwnSourceOfTruth(
    private val auth: AuthenticationRepository,
    private val dao: RecipeDao,
) : SourceOfTruth<Unit, List<RecipeDTO>, List<Recipe>> {

    override suspend fun delete(key: Unit) = Unit // Ignored.
    override suspend fun deleteAll() = Unit // Ignored.

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun reader(key: Unit): Flow<List<Recipe>?> = auth.identifier
        .map { it.identifier }
        .flatMapLatest { dao.recipesForUser(it) }
        .map { it.map { entity -> entity.toRecipe() } }

    override suspend fun write(key: Unit, value: List<RecipeDTO>) {
        // TODO : Handle recipe deletion from server.
        dao.recipesInsertAll(value.map { it.asRecipeEntity(inStack = null) })
    }
}
