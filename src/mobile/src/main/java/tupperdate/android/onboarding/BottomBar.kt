package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedButton

@Composable
fun BottomBar(
    buttonValue: String,
    onButtonClick: () -> Unit,
    bottomText: String
) {
    Column() {
        BrandedButton(
            value = buttonValue,
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
        )

        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled) {
            Text(
                text = bottomText,
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}
