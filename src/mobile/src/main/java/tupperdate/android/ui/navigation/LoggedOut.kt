package tupperdate.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import tupperdate.android.data.legacy.api.Api
import tupperdate.android.ui.onboarding.Onboarding
import tupperdate.android.ui.onboarding.OnboardingConfirmation

private object LoggedOutDestination {
    const val ONBOARDING = "onboarding"
    const val CONFIRMATION = "confirmation"
}

/**
 * The composable that manages the onboarding flow of the app.
 *
 * @param api the [Api] to manage data.
 */
@Composable
fun LoggedOut(
    api: Api,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LoggedOutDestination.ONBOARDING) {
        composable(LoggedOutDestination.ONBOARDING) {
            Onboarding(
                auth = api.authentication,
                verificationScreen = { navController.navigate(LoggedOutDestination.CONFIRMATION) }
            )
        }
        composable(LoggedOutDestination.CONFIRMATION) {
            OnboardingConfirmation(
                auth = api.authentication,
                onBack = {}
            )
        }
    }
}
