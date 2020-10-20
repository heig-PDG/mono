package tupperdate.android.appbars

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.GradientButton
import java.util.*

@Composable
fun oneButtonBottomBar(onButtonClick: () -> Unit, buttonString: String) {
    BottomAppBar(backgroundColor = Color.White)
    {
        GradientButton(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .preferredHeight(48.dp),
        ) {
            Text(
                text = buttonString.toUpperCase(Locale.getDefault())
            )
        }
    }
}

@Composable
fun oneButtonBottomBar(onButtonClick: () -> Unit, buttonString: String, textBelow: String) {
    Column() {
        GradientButton(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .preferredHeight(48.dp),
        ) {
            Text(
                text = (buttonString).toUpperCase(Locale.getDefault())
            )
        }

        Text(
            text = textBelow,
            style = TupperdateTypography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
    }
}