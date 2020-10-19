package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.ui.tooling.preview.Preview
import tupperdate.android.Destination
import tupperdate.android.home.Home
import tupperdate.android.ui.TupperdateTheme

@Composable
fun Onboarding(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column() {
        Text("This is the onboarding")
    }
}

@Preview
@Composable
private fun OnboardingPreview() {
    TupperdateTheme {
        Onboarding(
            onButtonClick = {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}