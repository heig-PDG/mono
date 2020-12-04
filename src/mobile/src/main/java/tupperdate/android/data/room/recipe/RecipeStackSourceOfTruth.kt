package tupperdate.android.data.room.recipe

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * An implementation of a [SourceOfTruth] that can manage groups of recipes (in the recipe stack)
 * and does not require a specific key to be fetched.
 *
 * @param dao the [RecipeDao] that is accessed by this [SourceOfTruth].
 */
class RecipeStackSourceOfTruth(
    private val dao: RecipeDao,
) : SourceOfTruth<Unit, List<RecipeDTO>, List<Recipe>> {

    override suspend fun delete(key: Unit) {
        // Ignore.
    }

    override suspend fun deleteAll() {
        // Ignore.
    }

    override fun reader(key: Unit): Flow<List<Recipe>?> {
        return dao.findAll()
    }

    override suspend fun write(key: Unit, value: List<RecipeDTO>) {
        dao.insertAll(value.map(RecipeDTO::asRecipe))
    }
}
