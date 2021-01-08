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
import tupperdate.android.ui.onboarding.OnboardingConfirmationViewModel.ConfirmationState.Pending

@Composable
@OptIn(ExperimentalCoroutinesApi::class)
fun OnboardingConfirmation(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val registration = AmbientPhoneRegistration.current
    val viewModel = getViewModel<OnboardingConfirmationViewModel> {
        parametersOf(registration)
    }
    val code by viewModel.code.collectAsState()
    val state by viewModel.state.collectAsState()

    OnboardingConfirmation(
        code = code,
        onCodeChanged = viewModel::onCodeInput,
        buttonText = when (state) {
            Pending -> stringResource(R.string.onboardingConfirmation_button_text_pending)
            else -> stringResource(R.string.onboardingConfirmation_button_text)
        },
        onButtonClick = viewModel::onSubmit,
        onBack = onBack,
        isErrorValue = isError(state),
        errorMsg = getErrorMsg(state),
        modifier = modifier,
    )
}
