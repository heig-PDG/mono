package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import tupperdate.api.auth.firebase

class RealApi(activity: AppCompatActivity) : Api {

    override val authentication = RealAuthenticationApi(activity, FirebaseAuth.getInstance())

    private val http = HttpClient {
        install(Auth) {
            firebase(authentication)
        }
        install(JsonFeature)
        defaultRequest {
            // TODO : Use HTTPS.
            host = "api.tupperdate.me"
            port = 80
            contentType(ContentType.parse("application/json"))
        }
    }

    override val users  = RealUserApi(http, authentication.uid)
    override val recipe = RealRecipeApi(http, activity)
    override val images = ActualImagePickerApi(activity)
}
