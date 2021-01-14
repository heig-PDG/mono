package tupperdate.web.facade.recipes

import org.koin.dsl.module
import tupperdate.web.facade.profiles.ProfileFacade
import tupperdate.web.facade.profiles.ProfileFacadeImpl

val KoinModuleFacadeRecipe = module {
    factory<RecipeFacade> { RecipeFacadeImpl(get()) }
}
