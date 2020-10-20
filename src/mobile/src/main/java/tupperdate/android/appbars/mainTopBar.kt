package tupperdate.android.appbars

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import tupperdate.android.R

@Composable
fun mainTopBar() {
    //commented because it doesn't find Modifier.weight, which however works in Onboarding.kt...
    /*TopAppBar(
        title = {
            Column(modifier = Modifier.weight(1f), horizontalGravity = Alignment.CenterHorizontally) {
                Text(text = "tupper.date", textAlign = TextAlign.Center)
            }
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(vectorResource(id = R.drawable.ic_chat_24px))
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(vectorResource(id = R.drawable.ic_account_circle_black_18dp))
            }
        },
        backgroundColor = Color.White
    )*/
}
