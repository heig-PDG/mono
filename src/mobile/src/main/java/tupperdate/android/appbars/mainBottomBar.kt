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
import tupperdate.android.home.BarButton
import tupperdate.android.home.littleButtonSize
import tupperdate.android.home.normalButtonSize
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
    //BottomAppBar(backgroundColor = Color.White)
    //{
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
        {
            BarButton(
                littleButtonSize,
                { onReturn() },
                Color.returnButton,
                { Icon(vectorResource(id = R.drawable.ic_return)) }
            )
            BarButton(
                normalButtonSize,
                { onDislike() },
                backgroundColor = Color.dislikeButton, {
                    Icon(vectorResource(id = R.drawable.ic_clear))
                })
            BarButton(
                normalButtonSize,
                { onLike() },
                Color.likeButton, {
                    Icon(vectorResource(id = R.drawable.ic_like))
                })
            BarButton(
                littleButtonSize,
                { onRecipeClick() },
                Color.recipeAddButton, {
                    Icon(vectorResource(id = R.drawable.ic_add_recipe))
                })
        }
    //}
}