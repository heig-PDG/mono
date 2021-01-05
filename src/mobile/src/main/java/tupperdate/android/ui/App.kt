package tupperdate.android.ui

import androidx.compose.runtime.Composable
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.ui.ambients.AmbientProfile
import tupperdate.android.ui.navigation.LoggedIn
import tupperdate.android.ui.navigation.LoggedOut

/**
 * The main composable of the app.
 */
@Composable
fun TupperdateApp() {
    when (AmbientProfile.current) {
        null -> {
            /* Still loading. */
        }
        is AuthenticationStatus.NoAuthentication -> {
            LoggedOut()
        }
        is AuthenticationStatus.NoProfile -> {
            // TODO : Display the screen to set the profile.
            LoggedOut()
        }
        is AuthenticationStatus.Profile -> {
            LoggedIn()
        }
    }
}
