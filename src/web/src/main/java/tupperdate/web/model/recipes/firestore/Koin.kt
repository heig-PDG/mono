package tupperdate.web.model.recipes.firestore

import org.koin.dsl.module
import tupperdate.web.model.recipes.RecipeRepository

val KoinModuleModelRecipesFirestore = module {
    factory<RecipeRepository> { FirestoreRecipeRepository(get(), get()) }
}
