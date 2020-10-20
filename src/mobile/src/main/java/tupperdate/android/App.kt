package tupperdate.android

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import kotlinx.coroutines.flow.Flow
import tupperdate.android.home.Home
import tupperdate.android.ui.BrandingPreview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.utils.Navigator
import tupperdate.api.Api
import tupperdate.api.AuthenticationApi

@Composable
fun TupperdateApp(
    api: Api,
    backDispatcher: OnBackPressedDispatcher
) {
    val navigator = rememberSavedInstanceState(
        saver = Navigator.saver<Destination>(backDispatcher)
    ) {
        Navigator(Destination.Home, backDispatcher)
    }

    val action = remember(navigator) { Action(navigator) }
    val user = remember { api.authentication.profile }

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
    destination : Destination,
    action: Action,
    user: Flow<AuthenticationApi.Profile?>
) {
    val currentUser by user.collectAsState(null)

    destination.let { dest ->
        when (dest) {
            is Destination.Home -> Home(
                action.viewPreview,
                currentUser
            )
            is Destination.BrandingPreview -> BrandingPreview()
        }
    }
}