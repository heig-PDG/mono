package tupperdate.web.koin

import org.koin.dsl.module
import tupperdate.web.facade.accounts.AccountFacade
import tupperdate.web.model.accounts.NotificationRepository
import tupperdate.web.model.accounts.PhoneRepository

val KoinModuleNotificationMock = module {
    factory<NotificationRepository> { NotificationRepositoryMock() }
}

val KoinModuleFacadeAccountMock = module {
    factory<AccountFacade> { AccountFacadeImplMock() }
}

val KoinModuleRepositoryPhoneMock = module {
    factory<PhoneRepository> { PhoneRepositoryImplMock() }
}
