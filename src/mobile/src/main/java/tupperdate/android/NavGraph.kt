package tupperdate.android

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tupperdate.android.utils.Navigator
import tupperdate.api.RecipeApi

// DESTINATIONS AVAILABLE WHEN THE USER IS LOGGED OUT

sealed class LoggedOutDestination : Parcelable {
    @Parcelize
    object BrandingPreview : LoggedOutDestination()

    @Parcelize
    object Onboarding : LoggedOutDestination()

    @Parcelize
    object OnboardingConfirmation : LoggedOutDestination()
}

class LoggedOutAction(private val navigator: Navigator<LoggedOutDestination>) {

    val viewOnboarding: () -> Unit = {
        navigator.navigate(LoggedOutDestination.Onboarding)
    }

    val viewConfirmation: () -> Unit = {
        navigator.navigate(LoggedOutDestination.OnboardingConfirmation)
    }

    val back: () -> Unit = {
        navigator.back()
    }
}

// DESTINATIONS AVAILABLE WHEN THE USER IS LOGGED IN

sealed class LoggedInDestination : Parcelable {

    @Parcelize
    object Profile : LoggedInDestination()

    @Parcelize
    object NewRecipe : LoggedInDestination()

    @Parcelize
    data class ViewRecipe(val recipe: RecipeApi.Recipe) : LoggedInDestination()

    @Parcelize
    object Home : LoggedInDestination()

    @Parcelize
    object AuthenticationTesting : LoggedInDestination()
}

class LoggedInAction(private val navigator: Navigator<LoggedInDestination>) {

    val newRecipe: () -> Unit = {
        navigator.navigate(LoggedInDestination.NewRecipe)
    }

    val viewRecipe: (RecipeApi.Recipe) -> Unit = {
        navigator.navigate(LoggedInDestination.ViewRecipe(it))
    }

    val home: () -> Unit = {
        navigator.navigate(LoggedInDestination.Home)
    }

    val back: () -> Unit = {
        navigator.back()
    }

    val profile: () -> Unit = {
        navigator.navigate(LoggedInDestination.Profile)
    }

    val authenticationTesting: () -> Unit = {
        navigator.navigate(LoggedInDestination.AuthenticationTesting)
    }
}
