package tupperdate.android.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tupperdate.android.data.features.messages.Match
import tupperdate.android.data.features.messages.MessagesRepository

class LoggedInViewModel(
    private val messages: MessagesRepository,
) : ViewModel() {

    // TODO : Introduce a grace delay in case of multiple matches.
    val match: Flow<Match?> = messages.pending.map { it.firstOrNull() }

    /**
     * Accepts a [Match], so that it will no longer be visible in a top-level dialog that indicates
     * that a new match has occurred.
     *
     * @param match the [Match] to accept.
     */
    fun onAccept(match: Match) {
        viewModelScope.launch {
            messages.accept(match)
        }
    }
}
