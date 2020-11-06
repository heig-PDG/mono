package tupperdate.android.onboarding

import androidx.compose.foundation.ProvideTextStyle
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
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedButton
import tupperdate.android.ui.material.BrandedTitleText
import tupperdate.api.AuthenticationApi
import tupperdate.api.api

private sealed class State
private data class Error(val error: Int) : State()
private object Pending : State()
private object WaitingForInput : State()

@Composable
fun Onboarding(
    auth: AuthenticationApi,
    verificationScreen: () -> Unit,
    loggedInScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val (phone, setPhone) = remember { mutableStateOf("") }
    val (state, setState) = remember { mutableStateOf<State>(WaitingForInput) }

    Onboarding(
        phone = phone,
        onPhoneInputValueChange = {
            setState(WaitingForInput)
            setPhone(it)
        },
        error = when (state) {
            is Error -> state.error
            else -> null
        },
        buttonText = when (state) {
            is Pending -> stringResource(R.string.onboarding_button_loading_text)
            else -> stringResource(R.string.onboarding_button_text)
        },
        onClick = {
            scope.launch {
                if (state != Pending) {
                    setState(Pending)
                    when (auth.requestCode(phone)) {
                        AuthenticationApi.RequestCodeResult.LoggedIn ->
                            loggedInScreen()
                        AuthenticationApi.RequestCodeResult.RequiresVerification ->
                            verificationScreen()
                        AuthenticationApi.RequestCodeResult.InvalidNumberError ->
                            setState(Error(R.string.onboarding_requestCode_error_invalid_number))
                        AuthenticationApi.RequestCodeResult.InternalError ->
                            setState(Error(R.string.onboarding_requestCode_error_internal))
                    }
                }
            }
        },
        modifier = modifier,
    )
}

/**
 * Stateless onboarding component
 */
@Composable
private fun Onboarding(
    phone: String,
    onPhoneInputValueChange: (String) -> Unit,
    buttonText: String,
    onClick: () -> Unit,
    error: Int?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(top = 72.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        ProvideTextStyle(TupperdateTypography.h4) {
            Text(
                text = stringResource(R.string.onboarding_welcome)
            )
            BrandedTitleText(
                text = stringResource(R.string.onboarding_welcome_name),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Text(
            text = stringResource(R.string.onboarding_presentation),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ViewPhoneInput(
            phone = phone,
            onValueChange = onPhoneInputValueChange,
            error = error,
        )

        Spacer(modifier = Modifier.weight(1f, true))

        BrandedButton(
            value = buttonText,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
        )

        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled) {
            Text(
                text = stringResource(R.string.onboarding_bottom_text),
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}

/**
 * A stateless input for a phone number
 *
 * @param phone Is the phone number to display
 * @param onValueChange Is executed when the value changes
 * @param error Is null if there is no error or corresponds to the string resource to display
 * @param modifier The modifier to apply
 */
@Composable
private fun ViewPhoneInput(
    phone: String,
    onValueChange: (String) -> Unit,
    error: Int?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = phone,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.onboarding_phone_label)) },
            placeholder = { Text(stringResource(R.string.onboarding_phone_placeholder)) },
            keyboardType = KeyboardType.Phone,
            isErrorValue = error != null,
            modifier = Modifier
                .fillMaxWidth()
        )

        error?.let {
            Text(
                text = stringResource(it),
                color = MaterialTheme.colors.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    // Use a real device to launch the preview.
    val owner = LifecycleOwnerAmbient.current
    val api = remember { owner.api() }

    TupperdateTheme {
        Onboarding(
            api.authentication,
            {},
            {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewPhoneInputNormalPreview() {
    val (phone, setPhone) = remember { mutableStateOf("") }

    TupperdateTheme {
        ViewPhoneInput(
            phone = phone,
            onValueChange = setPhone,
            error = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewPhoneInputNormalInvalidNumber() {
    val (phone, setPhone) = remember { mutableStateOf("") }

    TupperdateTheme {
        ViewPhoneInput(
            phone = phone,
            onValueChange = setPhone,
            error = R.string.onboarding_requestCode_error_invalid_number,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewPhoneInputNormalInternalError() {
    val (phone, setPhone) = remember { mutableStateOf("") }

    TupperdateTheme {
        ViewPhoneInput(
            phone = phone,
            onValueChange = setPhone,
            error = R.string.onboarding_requestCode_error_internal,
        )
    }
}
