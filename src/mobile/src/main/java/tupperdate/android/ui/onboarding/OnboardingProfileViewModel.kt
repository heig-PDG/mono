package tupperdate.android.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.picker.ImagePicker

class OnboardingProfileViewModel(
    private val picker: ImagePicker,
    private val auth: AuthenticationRepository,
) : ViewModel() {

    private val nameFlow = MutableStateFlow("")
    private val imageFlow = MutableStateFlow<ImagePicker.Handle?>(null)

    val name: StateFlow<String> = nameFlow
    val image: StateFlow<String?> = imageFlow.map { it?.uri?.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onNameChange(name: String) {
        nameFlow.value = name
    }

    fun onImageClick() {
        viewModelScope.launch {
            imageFlow.value = picker.pick()
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            auth.updateProfile(nameFlow.value, imageFlow.value)
        }
    }
}
