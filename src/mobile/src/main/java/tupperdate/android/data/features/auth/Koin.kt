package tupperdate.android.data.features.auth

import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.impl.FirebaseAuthenticationRepository

@OptIn(InternalDataApi::class)
val KoinModuleDataAuthentication = module {
    factory<AuthenticationRepository> { FirebaseAuthenticationRepository(get(), get()) }
}
