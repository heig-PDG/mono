package tupperdate.web.facade.accounts

import org.koin.dsl.module

val KoinModuleFacadeAccountReal = module {
    factory<AccountFacade> { AccountFacadeImplReal(get(), get()) }
}
