package tupperdate.android.appbars

import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.dislikeButton
import tupperdate.android.ui.likeButton
import tupperdate.android.ui.recipeAddButton
import tupperdate.android.ui.returnButton

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
            Button(
                modifier = Modifier.size(45.dp),
                onClick = onReturn,
                shape = CircleShape,
                backgroundColor = Color.returnButton
            ) {
                // TODO find a way to display these buttons in white
                Icon(vectorResource(id = R.drawable.ic_return))
            }
            Button(
                modifier = Modifier.size(50.dp),
                onClick = onDislike,
                shape = CircleShape,
                backgroundColor = Color.dislikeButton
            ) {
                Icon(vectorResource(id = R.drawable.ic_clear))
            }
            Button(
                modifier = Modifier.size(50.dp),
                onClick = onLike,
                shape = CircleShape,
                backgroundColor = Color.likeButton
            ) {
                Icon(vectorResource(id = R.drawable.ic_like))
            }
            Button(
                modifier = Modifier.size(45.dp),
                onClick = onRecipeClick,
                shape = CircleShape,
                backgroundColor = Color.recipeAddButton
            ) {
                Icon(vectorResource(id = R.drawable.ic_fastfood_white_18dp))
            }
        }
    }
}