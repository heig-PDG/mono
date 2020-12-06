package tupperdate.android.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.material.BrandedButton
import tupperdate.android.ui.theme.material.BrandedTitleText
import tupperdate.android.data.legacy.api.AuthenticationApi

private sealed class State
private data class Error(val error: LocalError) : State()
private object Pending : State()
private object WaitingForInput : State()

private enum class LocalError {
    Internal,
    InvalidNumber,
}

@Composable
private fun getErrorString(error: LocalError): String {
    return when (error) {
        LocalError.Internal -> stringResource(R.string.onboarding_error_internal)
        LocalError.InvalidNumber -> stringResource(R.string.onboarding_requestCode_error_invalid_number)
    }
}

@Composable
fun Onboarding(
    auth: AuthenticationApi,
    verificationScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = AmbientLifecycleOwner.current.lifecycleScope

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
                        AuthenticationApi.RequestCodeResult.LoggedIn -> Unit
                        AuthenticationApi.RequestCodeResult.RequiresVerification ->
                            verificationScreen()
                        AuthenticationApi.RequestCodeResult.InvalidNumberError ->
                            setState(Error(LocalError.InvalidNumber))
                        AuthenticationApi.RequestCodeResult.InternalError ->
                            setState(Error(LocalError.InvalidNumber))
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
    error: LocalError?,
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

        Providers(AmbientContentAlpha provides ContentAlpha.disabled) {
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
    error: LocalError?,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = phone,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.onboarding_phone_label)) },
            placeholder = { Text(stringResource(R.string.onboarding_phone_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isErrorValue = error != null,
            modifier = Modifier.fillMaxWidth(),
        )
        error?.let {
            Text(
                text = getErrorString(it),
                color = MaterialTheme.colors.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    // Use a real device to launch the preview.
    // TODO : Use a stateless preview.
    // val owner = LifecycleOwnerAmbient.current
    // val api = remember { owner.api() }
    // TupperdateTheme {
    //     Onboarding(
    //         api.authentication,
    //         {},
    //         {},
    //         Modifier.background(Color.White)
    //             .fillMaxSize()
    //     )
    // }
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
            error = LocalError.InvalidNumber,
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
            error = LocalError.Internal,
        )
    }
}
