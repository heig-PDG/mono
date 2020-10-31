package tupperdate.android

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import kotlinx.coroutines.flow.Flow
import tupperdate.android.home.Home
import tupperdate.android.onboarding.Onboarding
import tupperdate.android.onboarding.OnboardingConfirmation
import tupperdate.android.ui.BrandingPreview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.utils.Navigator
import tupperdate.api.Api
import tupperdate.api.AuthenticationApi

@Composable
fun TupperdateApp(
    api: Api,
    backDispatcher: OnBackPressedDispatcher,
) {
    val user = remember { api.authentication.profile }
    val currentUser by user.collectAsState(null)

    val navigator = rememberSavedInstanceState(
        saver = Navigator.saver(backDispatcher)
    ) {
        if (currentUser == null) {
            Navigator(Destination.Onboarding, backDispatcher)
        } else {
            Navigator(Destination.Home, backDispatcher)
        }
    }

    val action = remember(navigator) { Action(navigator) }

    TupperdateTheme {
        TupperdateAppDestination(
            api = api,
            destination = navigator.current,
            action = action,
            user = user,
        )
    }
}

@Composable
private fun TupperdateAppDestination(
    api: Api,
    destination: Destination,
    action: Action,
    user: Flow<AuthenticationApi.Profile?>
) {
    val currentUser by user.collectAsState(null)

    destination.let { dest ->
        when (dest) {
            is Destination.Home -> Home(
                onChatClick = {},
                onProfileClick = {},
                onLike = {},
                onDislike = {},
                onRecipeClick = {},
                onReturn = {}
            )
            is Destination.BrandingPreview -> BrandingPreview()
            is Destination.Onboarding -> Onboarding(
                onButtonClick = action.viewOnboardingConfirmation
            )
            is Destination.OnboardingConfirmation -> OnboardingConfirmation(
                onButtonClick = {}, // TODO: Add a behaviour to button
                onReturnClick = action.back
            )
        }
    }
}