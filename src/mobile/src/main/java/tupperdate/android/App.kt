package tupperdate.android

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import tupperdate.android.home.Home
import tupperdate.android.ui.BrandingPreview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.utils.Navigator
import tupperdate.api.Api

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
    val user = remember { api.authentication.connectedUser() }

    TupperdateTheme {
        TupperdateAppDestination(
            api = api,
            destination = navigator.current,
            action = action,
        )
    }
}

@Composable
private fun TupperdateAppDestination(
    api: Api,
    destination : Destination,
    action: Action,
) {
    destination.let { dest ->
        when (dest) {
            is Destination.Home -> Home(
                action.viewPreview
            )
            is Destination.BrandingPreview -> BrandingPreview()
        }
    }
}