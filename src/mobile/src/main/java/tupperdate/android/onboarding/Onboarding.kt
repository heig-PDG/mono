package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
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
import tupperdate.android.ui.material.GradientButton
import java.util.*

@Composable
fun Onboarding(
        onButtonClick: () -> Unit,
        modifier: Modifier = Modifier,
) {
    val (email, setEmail) = remember { mutableStateOf("") }

    Column(
            modifier.padding(top = 64.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
                text = "Welcome to Tupper • Date",
                style = TupperdateTypography.h3,
                modifier = Modifier.padding(bottom = 8.dp),
        )

        emailInput(email = email, onValueChange = setEmail)

        Text(
                text = "Discover folks who cook what you like, and start sharing meals with them today.",
                modifier = Modifier.padding(bottom = 8.dp)
        )


        Row(modifier = Modifier.weight(1f)) {}

        GradientButton(
                onClick = onButtonClick,
                modifier = Modifier
                        .fillMaxWidth()
                        .preferredHeight(48.dp),
        ) {
            Text(
                    text = ("Get started").toUpperCase(Locale.getDefault())
            )
        }

        Text(
                text = "This app was created during a group project at HEIG-VD. Make sure to check it out on GitHub.",
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
    }
}

@Composable
private fun emailInput(
        email: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
) {
    TextField(
            value = email,
            label = { Text(text = "Your email") },
            onValueChange = onValueChange,
            placeholder = { Text("john.aplleseed@tupperdate.me") },
            keyboardType = KeyboardType.Email,
            modifier = modifier.fillMaxWidth(),
    )
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