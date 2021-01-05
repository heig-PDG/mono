package tupperdate.android.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tupperdate.android.data.features.auth.PhoneRegistration

class OnboardingConfirmationViewModel(
    private val phone: PhoneRegistration,
) : ViewModel() {

    enum class ConfirmationState {
        WaitingForInput,
        Pending,
        VerificationError,
        InternalError,
    }

    private val current = MutableStateFlow(ConfirmationState.WaitingForInput)
    private val input = MutableStateFlow("")

    val state: StateFlow<ConfirmationState> = current
    val code: StateFlow<String> = input

    fun onCodeInput(input: String) {
        this.current.value = ConfirmationState.WaitingForInput
        this.input.value = input
    }

    fun onSubmit() {
        viewModelScope.launch {
            current.value = ConfirmationState.Pending
            when (phone.verify(input.value)) {
                PhoneRegistration.VerificationResult.LoggedIn -> Unit
                PhoneRegistration.VerificationResult.InvalidVerificationError ->
                    current.value = ConfirmationState.VerificationError
                PhoneRegistration.VerificationResult.InternalError ->
                    current.value = ConfirmationState.InternalError
            }
        }
    }
}
