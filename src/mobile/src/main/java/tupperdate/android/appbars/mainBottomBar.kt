package tupperdate.android.appbars

import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import tupperdate.android.R

@Composable
fun mainBottomBar() {
    BottomAppBar(backgroundColor = Color.White)
    {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
        {
            IconButton(onClick = {}) {
                Icon(vectorResource(id = R.drawable.ic_navigate_before_black_18dp))
            }
            IconButton(onClick = {}) {
                Icon(vectorResource(id = R.drawable.ic_clear_black_18dp))
            }
            IconButton(onClick = {}) {
                Icon(vectorResource(id = R.drawable.ic_favorite_border_black_18dp))
            }
            IconButton(onClick = {}) {
                Icon(vectorResource(id = R.drawable.ic_fastfood_black_18dp))
            }
        }
    }
}