package tupperdate.android.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.ui.ambients.AmbientImagePicker

@Composable
@OptIn(ExperimentalCoroutinesApi::class)
fun OnboardingProfile(
    modifier: Modifier = Modifier,
) {
    val picker = AmbientImagePicker.current
    val viewModel = getViewModel<OnboardingProfileViewModel> { parametersOf(picker) }
    val name by viewModel.name.collectAsState()
    val image by viewModel.image.collectAsState()
    OnboardingProfile(
        name = name,
        image = image ?: "", // TODO : Provide a good default.
        onNameChange = viewModel::onNameChange,
        onEditPictureClick = viewModel::onImageClick,
        onSaveClick = viewModel::onSaveClick,
        modifier = modifier,
    )
}
