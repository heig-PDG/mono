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
        AuthenticationStatus.Unknown, is AuthenticationStatus.LoadingProfile -> {
            /* Still loading. */
        }
        is AuthenticationStatus.None -> {
            LoggedOut()
        }
        is AuthenticationStatus.AbsentProfile -> {
            // Profile selection screen.
        }
        is AuthenticationStatus.CompleteProfile -> {
            LoggedIn()
        }
    }
}
