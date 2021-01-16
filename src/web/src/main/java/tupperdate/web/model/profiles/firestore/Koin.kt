package tupperdate.web.model.profiles.firestore

import org.koin.dsl.module
import tupperdate.web.model.profiles.UserRepository

val KoinModuleModelUsersFirestore = module {
    factory<UserRepository> { FirestoreUserRepository(get(), get(), get()) }
}
