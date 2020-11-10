package tupperdate.android.onboardingConfirmation

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import tupperdate.android.R
import tupperdate.android.appbars.onlyReturnTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedButton
import tupperdate.api.AuthenticationApi

private sealed class State
private object WaitingForInput : State()
private object Pending : State()
private object VerificationError : State()
private object InternalError : State()

private fun isError(state: State): Boolean {
    return when (state) {
        VerificationError -> true
        InternalError -> true
        else -> false
    }
}

@Composable
private fun getErrorMsg(state: State): String? {
    return when (state) {
        VerificationError -> stringResource(R.string.onboardingConfirmation_verification_error)
        InternalError -> stringResource(R.string.onboarding_error_internal)
        else -> null
    }
}

@Composable
fun OnboardingConfirmation(
    auth: AuthenticationApi,
    onReturnClick: () -> Unit,
    loggedInScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val (code, setCode) = remember { mutableStateOf("") }
    val (state, setState) = remember { mutableStateOf<State>(WaitingForInput) }

    OnboardingConfirmation(
        code = code,
        onCodeChanged = {
            setCode(it)
            setState(Pending)
        },
        onButtonClick = {
            scope.launch {
                setState(Pending)
                when (auth.verify(code)) {
                    AuthenticationApi.VerificationResult.LoggedIn -> loggedInScreen()
                    AuthenticationApi.VerificationResult.InvalidVerificationError ->
                        setState(VerificationError)
                    AuthenticationApi.VerificationResult.InternalError -> setState(InternalError)
                }
            }
        },
        onReturnClick = onReturnClick,
        isErrorValue = isError(state),
        errorMsg = getErrorMsg(state),
        modifier = modifier,
    )
}

@Composable
private fun OnboardingConfirmation(
    code: String,
    onCodeChanged: (String) -> Unit,
    onButtonClick: () -> Unit,
    onReturnClick: () -> Unit,
    isErrorValue: Boolean,
    errorMsg: String?,
    modifier: Modifier = Modifier,
) {

    onlyReturnTopBar(onReturnClick)

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

        codeInput(
            code = code,
            onCodeChanged = onCodeChanged,
            isErrorValue = isErrorValue,
            errorMsg = errorMsg,
        )

        Spacer(modifier = Modifier.weight(1f, true))

        BrandedButton(
            value = stringResource(R.string.onboardingConfirmation_button_text),
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
        )

        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled) {
            Text(
                text = stringResource(R.string.onboardingConfirmation_bottom_text),
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun codeInput(
    code: String,
    onCodeChanged: ((String)) -> Unit,
    isErrorValue: Boolean,
    errorMsg: String?,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = code,
        label = { Text(stringResource(R.string.onboardingConfirmation_code_label)) },
        onValueChange = onCodeChanged,
        placeholder = { Text(stringResource(R.string.onboardingConfirmation_code_placeholder)) },
        keyboardType = KeyboardType.Number,
        isErrorValue = isErrorValue,
        modifier = modifier.fillMaxWidth(),
    )

    errorMsg?.let {
        Text(
            text = it,
            color = MaterialTheme.colors.error,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingConfirmationPreview() {
    val (code, setCode) = remember { mutableStateOf("") }
    TupperdateTheme {
        OnboardingConfirmation(
            code = code,
            onCodeChanged = setCode,
            {},
            {},
            false,
            null,
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingConfirmationErrorPreview() {
    val (code, setCode) = remember { mutableStateOf("") }

    TupperdateTheme {
        OnboardingConfirmation(
            code = code,
            onCodeChanged = setCode,
            {},
            {},
            true,
            "This is a fictive error message",
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}
