package tupperdate.android.data.features.recipe.room

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * An implementation of a [SourceOfTruth] that can manage groups of recipes (in the recipe stack)
 * and does not require a specific key to be fetched.
 *
 * @param dao the [Dao] that is accessed by this [SourceOfTruth].
 */
@InternalDataApi
class RecipeStackSourceOfTruth(
    private val dao: Dao,
) : SourceOfTruth<Unit, List<RecipeDTO>, List<Recipe>> {

    override suspend fun delete(key: Unit) {
        // Ignore.
    }

    override suspend fun deleteAll() {
        // Ignore.
    }

    override fun reader(key: Unit): Flow<List<Recipe>?> {
        return dao.recipesStack().map { it.map(RecipeEntity::toRecipe) }
    }

    override suspend fun write(key: Unit, value: List<RecipeDTO>) {
        dao.recipesInsertAll(value.map { it.asRecipeEntity(inStack = true) })
    }
}
