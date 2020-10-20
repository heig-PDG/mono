package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedTextField
import tupperdate.android.ui.material.BrandedButton
import java.util.*

@Composable
fun Onboarding(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (phone, setPhone) = remember { mutableStateOf("") }

    Column(
        modifier.padding(top = 64.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Welcome to Tupper • Date",
            style = TupperdateTypography.h3,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = "Discover folks who cook what you like, and start sharing meals with them today.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BrandedTextField(
            value = phone,
            onValueChange = setPhone,
            label = { Text("Your telephone number") },
            placeholder = { Text("+41 79 123 45 67") },
            keyboardType = KeyboardType.Phone,
            modifier = Modifier
                .fillMaxWidth()
        )


        Row(modifier = Modifier.weight(1f)) {}

        BrandedButton(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .preferredHeight(48.dp),
        ) {
            Text(
                text = ("Get started").toUpperCase(Locale.getDefault())
            )
        }

        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled) {
            Text(
                text = "This app was created during a group project at HEIG-VD. Make sure to check it out on GitHub.",
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
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