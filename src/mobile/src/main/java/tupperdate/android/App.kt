package tupperdate.android

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.editRecipe.NewRecipe
import tupperdate.android.editRecipe.ViewRecipe
import tupperdate.android.home.Home
import tupperdate.android.onboarding.Onboarding
import tupperdate.android.onboardingConfirmation.OnboardingConfirmation
import tupperdate.android.profile.Profile
import tupperdate.android.testing.AuthenticationTesting
import tupperdate.android.ui.BrandingPreview
import tupperdate.android.utils.Navigator
import tupperdate.api.Api
import tupperdate.api.UserApi

/**
 * A sealed class representing the different states that the user interface can be in.
 */
private sealed class UiState {
    /**
     * The API has not yet loaded whether the user is currently logged in or not.
     */
    object Loading : UiState()

    /**
     * The user is not logged in yet.
     */
    object LoggedOut : UiState()

    /**
     * The user is logged in and has a valid [UserApi.Profile].
     */
    object LoggedIn : UiState()
}

/**
 * Transforms a [Flow] of [String], representing the [UserApi.Profile]'s uid, into a [UiState] that
 * can be consumed to display the appropriate UI.
 */
@Composable
private fun Flow<String?>.collectAsState(): UiState =
    map { if (it == null) UiState.LoggedOut else UiState.LoggedIn }
        .collectAsState(UiState.Loading).value

/**
 * The main composable of the app.
 *
 * @param api the [Api] instance to use.
 * @param backDispatcher an [OnBackPressedDispatcher] instance, used to integrate with back presses
 */
@Composable
fun TupperdateApp(
    api: Api,
    backDispatcher: OnBackPressedDispatcher,
) {
    val loggedIn = remember { api.authentication.uid }
    when (loggedIn.collectAsState()) {

        UiState.Loading -> {
            /* Display nothing. */
        }

        UiState.LoggedOut -> {
            // The user is currently logged out. Start at the Onboarding start.
            val nav = rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
                Navigator<LoggedOutDestination>(
                    LoggedOutDestination.Onboarding,
                    backDispatcher
                )
            }
            val act = remember(nav) { LoggedOutAction(nav) }
            LoggedOut(api, act, nav.current)
        }

        is UiState.LoggedIn -> {
            // The user is currently logged in. Start at the Home.
            val nav = rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
                Navigator<LoggedInDestination>(
                    LoggedInDestination.Home,
                    backDispatcher,
                )
            }
            val act = remember(nav) { LoggedInAction(nav) }
            LoggedIn(api, act, nav.current)
        }
    }
}

/**
 * The composable that manages the app navigation when the user is currently logged in.
 *
 * @param api the [Api] to manage data.
 * @param action the [LoggedInAction] available to the app.
 * @param destination the [LoggedInDestination] that we are currently on.
 */
@Composable
private fun LoggedIn(
    api: Api,
    action: LoggedInAction,
    destination: LoggedInDestination,
) {
    LaunchedEffect(true) { api.users.updateProfile() }

    val profile = remember { api.users.profile }.collectAsState(initial = null).value

    when (destination) {
        is LoggedInDestination.NewRecipe -> NewRecipe(
            recipeApi = api.recipe,
            imagePickerApi = api.images,
            onBack = action.back,
        )
        is LoggedInDestination.ViewRecipe -> ViewRecipe(
            recipeApi = api.recipe,
            recipe = destination.recipe,
            onBack = action.back,
        )
        is LoggedInDestination.Home -> Home(
            recipeApi = api.recipe,
            // TODO add behaviours on these buttons
            onChatClick = {},
            onProfileClick = action.profile,
            onRecipeClick = action.newRecipe,
            onReturnClick = {},
            onTitleClick = action.authenticationTesting,
            onRecipeDetailsClick = action.viewRecipe,
        )
        is LoggedInDestination.Profile ->
            Profile(
                userApi = api.users,
                imagePicker = api.images,
                profile = profile ?: api.users.emptyProfile,
                onCloseClick = action.back,
                onSignOutClick = {},
            )

        is LoggedInDestination.AuthenticationTesting ->
            AuthenticationTesting(api)
    }
}

/**
 * The composable that manages the onboarding flow of the app.
 *
 * @param api the [Api] to manage data.
 * @param action the [LoggedOutAction] available to navigate.
 * @param destination the [LoggedOutDestination] that we are currently on.
 */
@Composable
private fun LoggedOut(
    api: Api,
    action: LoggedOutAction,
    destination: LoggedOutDestination,
) {
    when (destination) {
        LoggedOutDestination.BrandingPreview -> BrandingPreview()
        LoggedOutDestination.Onboarding -> Onboarding(
            auth = api.authentication,
            verificationScreen = action.viewConfirmation,
        )
        LoggedOutDestination.OnboardingConfirmation -> OnboardingConfirmation(
            auth = api.authentication,
            onReturnClick = action.back,
        )
    }
}
