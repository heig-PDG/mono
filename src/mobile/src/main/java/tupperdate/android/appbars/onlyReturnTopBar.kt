package tupperdate.android.appbars

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
    )
}