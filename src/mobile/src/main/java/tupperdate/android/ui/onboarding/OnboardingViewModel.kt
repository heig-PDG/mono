package tupperdate.android.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tupperdate.android.data.features.auth.PhoneRegistration
import tupperdate.android.ui.onboarding.OnboardingViewModel.State.*

class OnboardingViewModel(
    private val onCheckCode: () -> Unit,
    private val api: PhoneRegistration,
) : ViewModel() {

    sealed class State {
        data class Error(val error: LocalError) : State()
        object Pending : State()
        object WaitingForInput : State()
    }

    enum class LocalError {
        Internal,
        InvalidNumber,
    }

    private val current = MutableStateFlow<State>(WaitingForInput)
    private val input = MutableStateFlow("")
    private val mutex = Mutex()

    val state: StateFlow<State> = current
    val phone: StateFlow<String> = input

    fun onPhoneChanged(input: String) {
        this.current.value = WaitingForInput
        this.input.value = input
    }

    fun onConfirm() {
        viewModelScope.launch {
            mutex.withLock {
                if (current.value == Pending) return@launch
                current.value = Pending
                when (api.requestCode(input.value, false)) {
                    PhoneRegistration.RequestCodeResult.LoggedIn -> Unit
                    PhoneRegistration.RequestCodeResult.RequiresVerification -> onCheckCode()
                    PhoneRegistration.RequestCodeResult.InvalidNumberError ->
                        current.value = Error(LocalError.InvalidNumber)
                    PhoneRegistration.RequestCodeResult.InternalError ->
                        current.value = Error(LocalError.Internal)
                }
            }
        }
    }
}
