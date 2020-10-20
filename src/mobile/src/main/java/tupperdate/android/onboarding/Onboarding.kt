package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedTextField

@Composable
fun Onboarding(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (phone, setPhone) = remember { mutableStateOf("") }

    Column(
        modifier.padding(top = 64.dp, bottom = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.onboarding_welcome),
            style = TupperdateTypography.h3,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = stringResource(R.string.onboarding_presentation),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BrandedTextField(
            value = phone,
            onValueChange = setPhone,
            label = { Text(stringResource(R.string.onboarding_phone_label)) },
            placeholder = { Text(stringResource(R.string.onboarding_phone_placeholder)) },
            keyboardType = KeyboardType.Phone,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f, true))

        BottomBar(
            buttonValue = stringResource(R.string.onboarding_button_text),
            onButtonClick = onButtonClick,
            bottomText = stringResource(R.string.onboarding_bottom_text),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    TupperdateTheme {
        Onboarding(
            {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}