package tupperdate.android.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.R
import tupperdate.android.ui.ambients.AmbientPhoneRegistration
import tupperdate.android.ui.onboarding.OnboardingViewModel.State

@Composable
@OptIn(ExperimentalCoroutinesApi::class)
fun Onboarding(
    verificationScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val registration = AmbientPhoneRegistration.current
    val viewModel = getViewModel<OnboardingViewModel> {
        parametersOf(
            verificationScreen,
            registration,
        )
    }

    val phone by viewModel.phone.collectAsState()
    val state by viewModel.state.collectAsState()

    Onboarding(
        phone = phone,
        onPhoneInputValueChange = viewModel::onPhoneChanged,
        error = when (val s = state) {
            is State.Error -> s.error
            else -> null
        },
        buttonText = when (state) {
            is State.Pending -> stringResource(R.string.onboarding_button_loading_text)
            else -> stringResource(R.string.onboarding_button_text)
        },
        onClick = viewModel::onConfirm,
        modifier = modifier,
    )
}
