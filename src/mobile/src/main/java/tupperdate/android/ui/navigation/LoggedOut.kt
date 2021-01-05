package tupperdate.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import tupperdate.android.ui.onboarding.Onboarding
import tupperdate.android.ui.onboarding.OnboardingConfirmation

/**
 * Available destinations when the user is logged out
 */
private object LoggedOutDestination {
    const val ONBOARDING = "onboarding"
    const val CONFIRMATION = "confirmation"
}

/**
 * The composable that manages the onboarding flow of the app.
 */
@Composable
fun LoggedOut() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LoggedOutDestination.ONBOARDING) {
        composable(LoggedOutDestination.ONBOARDING) {
            Onboarding(
                verificationScreen = { navController.navigate(LoggedOutDestination.CONFIRMATION) }
            )
        }
        composable(LoggedOutDestination.CONFIRMATION) {
            OnboardingConfirmation(
                onBack = { navController.navigateUp() }
            )
        }
    }
}
