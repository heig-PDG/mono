package tupperdate.android.testing

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.lifecycle.lifecycleScope
import androidx.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import tupperdate.api.Api
import tupperdate.api.AuthenticationApi
import tupperdate.api.api

@Composable
private fun AuthenticationTesting(
    api: Api,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val (phone, setPhone) = remember { mutableStateOf("") }
    val (verification, setVerification) = remember { mutableStateOf("") }

    val (requestCodeResult, setRequestCodeResult) = remember {
        mutableStateOf<AuthenticationApi.RequestCodeResult?>(null)
    }
    val (verificationResult, setVerificationResult) = remember {
        mutableStateOf<AuthenticationApi.VerificationResult?>(null)
    }
    val profile by remember { api.authentication.profile }.collectAsState(null)

    Column(modifier) {
        TextField(
            value = phone,
            onValueChange = setPhone,
            label = { Text("Phone") },
        )
        RequestCodeStatus(requestCodeResult)
        TextField(
            value = verification,
            onValueChange = setVerification,
            label = { Text("Verification code") },
        )
        VerificationResultStatus(verificationResult)
        Button(onClick = {
            scope.launch {
                setRequestCodeResult(null)
                setRequestCodeResult(api.authentication.requestCode(phone))
            }
        }) { Text("Send code") }
        Button(onClick = {
            scope.launch {
                setVerificationResult(null)
                setVerificationResult(api.authentication.verify(verification))
            }
        }) { Text("Verify code") }
        Profile(profile)
    }
}

@Composable
private fun RequestCodeStatus(
    state: AuthenticationApi.RequestCodeResult?,
    modifier: Modifier = Modifier,
) {
    val text = when (state) {
        AuthenticationApi.RequestCodeResult.LoggedIn -> "LoggedIn"
        AuthenticationApi.RequestCodeResult.RequiresVerification -> "RequiresVerification"
        AuthenticationApi.RequestCodeResult.InternalError -> "InternalError"
        AuthenticationApi.RequestCodeResult.InvalidNumberError -> "InvalidNumberError"
        else -> "Not set"
    }
    Text("RequestCodeStatus $text", modifier)
}

@Composable
private fun VerificationResultStatus(
    state: AuthenticationApi.VerificationResult?,
    modifier: Modifier = Modifier,
) {
    val text = when (state) {
        AuthenticationApi.VerificationResult.LoggedIn -> "LoggedIn"
        AuthenticationApi.VerificationResult.InvalidVerificationError -> "InvalidVerificationError"
        AuthenticationApi.VerificationResult.InternalError -> "InternalError"
        null -> "Not set"
    }
    Text("VerificationStatus $text", modifier)
}

@Composable
private fun Profile(
    state: AuthenticationApi.Profile?,
    modifier: Modifier = Modifier,
) {
    Text("Profile $state", modifier)
}

@Composable
@Preview
private fun AuthenticationTestingPreview() {
    // Use a real device to launch the preview.
    // val owner = LifecycleOwnerAmbient.current
    // val api = remember { owner.api() }
    //
    // AuthenticationTesting(api, Modifier.fillMaxSize())
}
