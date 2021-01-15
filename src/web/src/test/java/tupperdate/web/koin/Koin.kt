package tupperdate.web.koin

import org.koin.dsl.module
import tupperdate.web.facade.accounts.AccountFacade


val KoinModuleFacadeAccountMock = module {
    factory<AccountFacade> { AccountFacadeImplMock() }
}
