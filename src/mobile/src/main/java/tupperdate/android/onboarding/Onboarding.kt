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

private enum class Error {
    Internal,
    InvalidNumber,
}

@Composable
private fun getErrorString(error: Error) : String {
    return when (error) {
        Error.Internal -> stringResource(R.string.onboarding_requestCode_error_internal)
        Error.InvalidNumber -> stringResource(R.string.onboarding_requestCode_error_invalid_number)
    }
}

@Composable
fun Onboarding(
    auth: AuthenticationApi,
    verificationScreen: () -> Unit,
    loggedInScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope
    val (pending, setPending) = remember { mutableStateOf(false) }
    val (phone, setPhone) = remember { mutableStateOf("") }

    val (requestCodeResult, setRequestCodeResult) = remember {
        mutableStateOf<AuthenticationApi.RequestCodeResult?>(null)
    }

    when (requestCodeResult) {
        AuthenticationApi.RequestCodeResult.LoggedIn -> loggedInScreen()
        AuthenticationApi.RequestCodeResult.RequiresVerification -> verificationScreen()
        AuthenticationApi.RequestCodeResult.InvalidNumberError -> Unit
        AuthenticationApi.RequestCodeResult.InternalError -> Unit
        null -> Unit
    }
    val error = when (requestCodeResult) {
        AuthenticationApi.RequestCodeResult.InvalidNumberError -> Error.InvalidNumber
        AuthenticationApi.RequestCodeResult.InternalError -> Error.Internal
        else -> null
    }

    Onboarding(
        pending = pending,
        setPending = setPending,
        phone = phone,
        setPhone = setPhone,
        requestCodeResult = requestCodeResult,
        setRequestCodeResult = setRequestCodeResult,
        requestCode = { code -> scope.launch { setRequestCodeResult(auth.requestCode(code)) } },
        error = error,
        modifier = modifier,
    )
}

@Composable
private fun Onboarding(
    pending: Boolean,
    setPending: (Boolean) -> Unit,
    phone: String,
    setPhone: (String) -> Unit,
    requestCodeResult: AuthenticationApi.RequestCodeResult?,
    setRequestCodeResult: (AuthenticationApi.RequestCodeResult?) -> Unit,
    requestCode: (String) -> Unit,
    error: Error?,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier.padding(top = 72.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        ProvideTextStyle(TupperdateTypography.h4) {
            Text(stringResource(R.string.onboarding_welcome))
            BrandedTitleText(
                stringResource(R.string.onboarding_welcome_name),
                Modifier.padding(bottom = 16.dp)
            )
        }

        Text(
            text = stringResource(R.string.onboarding_presentation),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ViewPhoneInput(
            phone = phone,
            setPhone = setPhone,
            setSentRequest = setPending,
            setCodeResult = setRequestCodeResult,
            error = error,
        )

        Spacer(modifier = Modifier.weight(1f, true))

        BrandedButton(
            value = if (pending && requestCodeResult == null) {
                stringResource(R.string.onboarding_button_loading_text)
            } else {
                stringResource(R.string.onboarding_button_text)
            },
            onClick = {
                setPending(true)
                if (requestCodeResult == null) {
                    requestCode(phone)
                }
            },
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

@Composable
private fun ViewPhoneInput(
    phone: String,
    setPhone: (String) -> Unit,
    setSentRequest: (Boolean) -> Unit,
    setCodeResult: (AuthenticationApi.RequestCodeResult?) -> Unit,
    error: Error?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = phone,
            onValueChange = {
                setCodeResult(null)
                setSentRequest(false)
                setPhone(it)
            },
            label = { Text(stringResource(R.string.onboarding_phone_label)) },
            placeholder = { Text(stringResource(R.string.onboarding_phone_placeholder)) },
            keyboardType = KeyboardType.Phone,
            isErrorValue = error != null,
            modifier = Modifier
                .fillMaxWidth()
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
            setPhone = setPhone,
            setCodeResult = {},
            setSentRequest = {},
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
            setPhone = setPhone,
            setCodeResult = {},
            setSentRequest = {},
            error = Error.InvalidNumber,
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
            setPhone = setPhone,
            setCodeResult = {},
            setSentRequest = {},
            error = Error.Internal,
        )
    }
}
