package tupperdate.android.appbars

import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R

@Composable
fun mainBottomBar(
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onReturn: () -> Unit,
    onRecipeClick: () -> Unit
) {
    BottomAppBar(backgroundColor = Color.White)
    {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
        {
            Button(modifier = Modifier.size(45.dp), onClick = onReturn, shape = CircleShape, backgroundColor = Color(
                197, 180, 246)) {
                Icon(vectorResource(id = R.drawable.ic_navigate_before_white_18dp))
            }
            Button(modifier = Modifier.size(50.dp), onClick = onDislike, shape= CircleShape, backgroundColor = Color(
                252, 90, 100)) {
                Icon(vectorResource(id = R.drawable.ic_clear_white_18dp))
            }
            Button(modifier = Modifier.size(50.dp), onClick = onLike, shape = CircleShape, backgroundColor = Color(
                142, 200, 255)) {
                Icon(vectorResource(id = R.drawable.ic_favorite_border_white_18dp))
            }
            Button(modifier = Modifier.size(45.dp), onClick = onRecipeClick, shape = CircleShape, backgroundColor = Color(
                223, 189, 157)) {
                Icon(vectorResource(id = R.drawable.ic_fastfood_white_18dp))
            }
        }
    }
}