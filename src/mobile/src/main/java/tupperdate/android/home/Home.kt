package tupperdate.android.home

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme

@Composable
fun Home(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(2.dp)
    ) {
        Text(text = "Hi There")
        Button(onClick = onButtonClick) {
            Text("View Branding")
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    TupperdateTheme {
        Home(
            onButtonClick = {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}