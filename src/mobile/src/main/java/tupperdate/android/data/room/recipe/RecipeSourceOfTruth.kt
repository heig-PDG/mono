package tupperdate.android.data.room.recipe

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.Recipe
import tupperdate.common.dto.RecipeDTO

class RecipeSourceOfTruth(
    private val dao: RecipeDao,
) : SourceOfTruth<String, RecipeDTO, Recipe> {

    override suspend fun delete(key: String) {
        dao.delete(key)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override fun reader(key: String): Flow<Recipe?> {
        return dao.find(key)
    }

    override suspend fun write(key: String, value: RecipeDTO) {
        dao.insert(value.asRecipe())
    }
}
