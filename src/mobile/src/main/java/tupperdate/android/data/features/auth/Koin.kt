package tupperdate.android.data.features.auth

import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseAuthenticationRepository

@OptIn(InternalDataApi::class)
val KoinModuleDataAuthentication = module {
    single<AuthenticationRepository> { FirebaseAuthenticationRepository(get(), get(), get()) }
}
