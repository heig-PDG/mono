package tupperdate.android.ui.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tupperdate.android.data.features.auth.AuthenticationRepository

// TODO : Use an image picker here.
class ProfileViewModel(
    private val auth: AuthenticationRepository,
) : ViewModel() {

    private val currentlyEditing = MutableStateFlow(false)

    val editing: Flow<Boolean> = currentlyEditing

    fun onEditClick() {
        currentlyEditing.value = true
    }

    fun onSave(
        name: String,
    ) {
        viewModelScope.launch {
            // TODO : Upload new images.
            auth.updateProfile(displayName = name)
            currentlyEditing.value = false
        }
    }
}
