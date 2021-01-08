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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.material.BrandedButton
import tupperdate.android.ui.theme.material.BrandedTitleText

@Composable
private fun getErrorString(error: OnboardingViewModel.LocalError): String {
    return when (error) {
        OnboardingViewModel.LocalError.Internal -> stringResource(R.string.onboarding_error_internal)
        OnboardingViewModel.LocalError.InvalidNumber -> stringResource(R.string.onboarding_requestCode_error_invalid_number)
    }
}

/**
 * Stateless onboarding component
 */
@Composable
fun Onboarding(
    phone: String,
    onPhoneInputValueChange: (String) -> Unit,
    buttonText: String,
    onClick: () -> Unit,
    error: OnboardingViewModel.LocalError?,
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
    error: OnboardingViewModel.LocalError?,
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
            error = OnboardingViewModel.LocalError.InvalidNumber,
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
            error = OnboardingViewModel.LocalError.Internal,
        )
    }
}
