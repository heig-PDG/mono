package tupperdate.web.facade.recipes

import org.koin.dsl.module

val KoinModuleFacadeRecipe = module {
    factory<RecipeFacade> { RecipeFacadeImpl(get(), get()) }
}
