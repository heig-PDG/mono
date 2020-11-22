package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

class RealApi(activity: AppCompatActivity) : Api {
    private val http = HttpClient {
        install(JsonFeature)
        defaultRequest {
            // TODO : Use HTTPS.
            host = "api.tupperdate.me"
            port = 80
        }
    }
    override val authentication = RealAuthenticationApi(activity, FirebaseAuth.getInstance())
    override val recipe = RealRecipeApi(authentication, http)
    override val images = ActualImagePickerApi(activity)
}
