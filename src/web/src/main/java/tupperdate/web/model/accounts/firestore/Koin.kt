package tupperdate.web.model.accounts.firestore

import org.koin.dsl.module
import tupperdate.web.model.accounts.AuthRepository
import tupperdate.web.model.accounts.PhoneRepository

val KoinModuleModelPhonesFirebase = module {
    factory<PhoneRepository> { FirebasePhoneRepository(get()) }
}

val KoinModuleModelAuthFirebase = module {
    factory<AuthRepository> { FirebaseAuthRepository(get()) }
}
