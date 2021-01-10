package tupperdate.android.data.features.recipe.room

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * An implementation of a [SourceOfTruth] that can manage individual recipes, with their unique
 * keys.
 *
 * @param dao the [RecipeDao] that is accessed by this [SourceOfTruth].
 */
@InternalDataApi
class RecipeSourceOfTruth(
    private val dao: RecipeDao,
) : SourceOfTruth<String, RecipeDTO, Recipe> {

    override suspend fun delete(key: String) {
        dao.recipesDelete(key)
    }

    override suspend fun deleteAll() {
        dao.recipesDeleteAll()
    }

    override fun reader(key: String): Flow<Recipe?> {
        return dao.recipe(forId = key).map(RecipeEntity::toRecipe)
    }

    override suspend fun write(key: String, value: RecipeDTO) {
        // Let the DAO handle upsertions as required.
        dao.recipesInsert(value.asRecipeEntity(inStack = null))
    }
}
