package tupperdate.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.setContent
import org.koin.android.ext.android.get
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.impl.FirebaseAuthenticationRepository
import tupperdate.android.data.features.auth.impl.FirebasePhoneRegistrationApi
import tupperdate.android.data.features.picker.impl.ImagePickerImpl
import tupperdate.android.ui.ambients.AmbientImagePicker
import tupperdate.android.ui.ambients.AmbientPhoneRegistration
import tupperdate.android.ui.ambients.AmbientProfile
import tupperdate.android.ui.theme.TupperdateTheme

class MainActivity : AppCompatActivity() {

    @OptIn(InternalDataApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO : Eventually clean this up and use better names.
        val phone = FirebasePhoneRegistrationApi(this)
        val picker = ImagePickerImpl(this)
        val authApi = FirebaseAuthenticationRepository(get(), get())

        setContent {
            val profile by authApi.status.collectAsState(null)
            Providers(
                // Provide API ambients that require an instance of an Activity to properly work.
                // This way, we don't have any memory leaks, and avoid duplicate calls to
                // Koin.startKoin
                AmbientPhoneRegistration provides phone,
                AmbientImagePicker provides picker,

                // Not necessary, but generally useful since this information is used in a lot of
                // places in our composition.
                AmbientProfile provides profile,
            ) {
                // Display our app, wrapped in the standard branding.
                TupperdateTheme { TupperdateApp() }
            }
        }
    }
}
