package tupperdate.android.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tupperdate.android.data.legacy.api.Api
import tupperdate.android.data.legacy.api.UserApi
import tupperdate.android.ui.navigation.LoggedIn
import tupperdate.android.ui.navigation.LoggedOut

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
            LoggedOut(api = api)
        }

        is UiState.LoggedIn -> {
            // The user is currently logged in. Start at the Home.
            LoggedIn(api)
        }
    }
}
