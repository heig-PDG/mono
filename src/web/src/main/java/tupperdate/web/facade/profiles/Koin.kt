package tupperdate.web.facade.profiles

import org.koin.dsl.module

val KoinModuleFacadeProfile = module {
    factory<ProfileFacade> { ProfileFacadeImpl(get(), get()) }
}
