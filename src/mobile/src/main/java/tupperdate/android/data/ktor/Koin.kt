package tupperdate.android.data.ktor

import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.dsl.module
import tupperdate.android.data.legacy.RealAuthenticationApi
import tupperdate.android.data.legacy.auth.firebase

/**
 * A Koin module that prepares the [HttpClient] that will be used to perform requests throughout
 * the application.
 */
val KoinKtorModule = module {
    single {
        HttpClient {
            install(Auth) { firebase(RealAuthenticationApi(get(), FirebaseAuth.getInstance())) }
            install(JsonFeature)
            defaultRequest {
                // TODO : Use HTTPS.
                host = "api.tupperdate.me"
                port = 80
                contentType(ContentType.parse("application/json"))
            }
        }
    }
}
