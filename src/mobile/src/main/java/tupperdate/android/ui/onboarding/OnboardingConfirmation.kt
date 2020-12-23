package tupperdate.android.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tupperdate.android.R
import tupperdate.android.data.legacy.api.AuthenticationApi
import tupperdate.android.ui.onboarding.ConfirmationState.*
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.material.BrandedButton

private enum class ConfirmationState {
    WaitingForInput,
    Pending,
    VerificationError,
    InternalError,
}

@Composable
private fun getErrorMsg(state: ConfirmationState): String? {
    return when (state) {
        VerificationError -> stringResource(R.string.onboardingConfirmation_verification_error)
        InternalError -> stringResource(R.string.onboarding_error_internal)
        else -> null
    }
}

@Composable
fun OnboardingConfirmation(
    auth: AuthenticationApi,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = AmbientLifecycleOwner.current.lifecycleScope

    val (code, setCode) = remember { mutableStateOf("") }
    val (state, setState) = remember { mutableStateOf(WaitingForInput) }

    OnboardingConfirmation(
        code = code,
        onCodeChanged = {
            setCode(it)
            setState(WaitingForInput)
        },
        buttonText = when (state) {
            Pending -> stringResource(R.string.onboardingConfirmation_button_text_pending)
            else -> stringResource(R.string.onboardingConfirmation_button_text)
        },
        onButtonClick = {
            scope.launch {
                setState(Pending)
                when (auth.verify(code)) {
                    AuthenticationApi.VerificationResult.LoggedIn -> Unit
                    AuthenticationApi.VerificationResult.InvalidVerificationError ->
                        setState(VerificationError)
                    AuthenticationApi.VerificationResult.InternalError -> setState(InternalError)
                }
            }
        },
        onBack = onBack,
        error = getErrorMsg(state),
        modifier = modifier,
    )
}

@Composable
private fun OnboardingConfirmation(
    code: String,
    onCodeChanged: (String) -> Unit,
    buttonText: String,
    onButtonClick: () -> Unit,
    onBack: () -> Unit,
    error: String?,
    modifier: Modifier = Modifier,
) {

    TopAppBar(
        title = {},
        navigationIcon = { IconButton(onBack) { Icon(vectorResource(R.drawable.ic_back_arrow)) } },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
    )

    Column(
        modifier.padding(top = 102.dp, bottom = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.onboardingConfirmation_title),
            style = TupperdateTypography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.onboardingConfirmation_reception_text),
            modifier = Modifier.padding(bottom = 38.dp)
        )

        OutlinedTextField(
            value = code,
            label = { Text(stringResource(R.string.onboardingConfirmation_code_label)) },
            onValueChange = onCodeChanged,
            placeholder = { Text(stringResource(R.string.onboardingConfirmation_code_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isErrorValue = error != null,
            modifier = Modifier.fillMaxWidth(),
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.error,
            )
        }

        Spacer(modifier = Modifier.weight(1f, true))

        BrandedButton(
            value = buttonText,
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
        )

        Providers(AmbientContentAlpha provides ContentAlpha.disabled) {
            Text(
                text = stringResource(R.string.onboardingConfirmation_bottom_text),
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingConfirmationPreview() = TupperdateTheme {
    val (code, setCode) = remember { mutableStateOf("") }
    OnboardingConfirmation(
        code = code,
        onCodeChanged = setCode,
        buttonText = "Button text",
        onButtonClick = {},
        onBack = {},
        error = null,
        modifier = Modifier.background(Color.White)
            .fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun OnboardingConfirmationErrorPreview() = TupperdateTheme {
    val (code, setCode) = remember { mutableStateOf("") }
    OnboardingConfirmation(
        code = code,
        onCodeChanged = setCode,
        buttonText = "Button text",
        onButtonClick = {},
        onBack = {},
        error = "This is a fictive error message",
        modifier = Modifier.background(Color.White)
            .fillMaxSize()
    )
}
