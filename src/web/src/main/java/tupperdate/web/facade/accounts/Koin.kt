package tupperdate.web.facade.accounts

import org.koin.dsl.module

val KoinModuleFacadeAccount = module {
    factory<AccountFacade> { AccountFacadeImpl(get()) }
}
