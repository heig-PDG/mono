package tupperdate.android.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val current = MutableStateFlow(HomeSection.Feed)

    val section: StateFlow<HomeSection> = current

    fun onSectionSelected(section: HomeSection) {
        current.value = section
    }
}
