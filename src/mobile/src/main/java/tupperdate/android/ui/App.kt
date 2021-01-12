package tupperdate.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.ui.ambients.AmbientProfile
import tupperdate.android.ui.navigation.LoggedIn
import tupperdate.android.ui.navigation.LoggedOut
import tupperdate.android.ui.onboarding.OnboardingProfile

/**
 * The main composable of the app.
 */
@Composable
fun TupperdateApp() {
    Crossfade(AmbientProfile.current) { profile ->
        when (profile) {
            AuthenticationStatus.Unknown, is AuthenticationStatus.LoadingProfile -> {
                /* Ignored. */
            }
            is AuthenticationStatus.None -> {
                LoggedOut()
            }
            is AuthenticationStatus.AbsentProfile -> {
                OnboardingProfile()
            }
            is AuthenticationStatus.CompleteProfile -> {
                LoggedIn()
            }
        }
    }
}
