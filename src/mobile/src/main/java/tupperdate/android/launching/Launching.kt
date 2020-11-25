package tupperdate.android.launching

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R

@ExperimentalAnimationApi
@Composable
fun Launching(modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = true) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                asset = vectorResource(R.drawable.ic_logo),
                alignment = Alignment.Center,
                modifier = Modifier.preferredSize(146.dp, 146.dp)
            )
        }
    }
}
