package tupperdate.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.ui.UserFlow.*
import tupperdate.android.ui.ambients.AmbientProfile
import tupperdate.android.ui.navigation.LoggedIn
import tupperdate.android.ui.navigation.LoggedOut
import tupperdate.android.ui.onboarding.OnboardingProfile

/**
 * The four main stories of our application.
 */
private enum class UserFlow {
    Splash,
    Onboarding,
    MissingProfile,
    Main,
}

/**
 * Maps the current [AuthenticationStatus] to a [UserFlow] that corresponds to what should be
 * displayed on the screen.
 */
private fun AuthenticationStatus.toUserFlow() = when (this) {
    AuthenticationStatus.Unknown, is AuthenticationStatus.LoadingProfile -> Splash
    is AuthenticationStatus.None -> Onboarding
    is AuthenticationStatus.AbsentProfile -> MissingProfile
    is AuthenticationStatus.CompleteProfile -> Main
}

/**
 * The main composable of the app.
 */
@Composable
fun TupperdateApp() {
    Crossfade(AmbientProfile.current.toUserFlow()) { profile ->
        when (profile) {
            Splash -> Unit
            Onboarding -> LoggedOut()
            MissingProfile -> OnboardingProfile()
            Main -> LoggedIn()
        }
    }
}
