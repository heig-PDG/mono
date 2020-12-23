package tupperdate.android.ui.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {

    enum class ButtonLabel {
        GetStarted,
        Loading,
        Verifying,
        LetsGo,
    }

    fun buttonLabel(): StateFlow<ButtonLabel> {
        return MutableStateFlow(ButtonLabel.GetStarted)
    }
}
