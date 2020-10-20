package tupperdate.android.appbars

import androidx.compose.foundation.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import tupperdate.android.R

@Composable
fun onlyReturnTopBar(onReturnClick: () -> Unit,) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onReturnClick) {
                Icon(vectorResource(id = R.drawable.ic_navigate_before_black_18dp))
            }
        },
        backgroundColor = Color.White
    )
}