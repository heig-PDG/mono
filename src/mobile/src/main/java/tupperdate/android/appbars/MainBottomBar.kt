package tupperdate.android.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import tupperdate.android.R
import tupperdate.android.home.BarButton
import tupperdate.android.home.LittleButtonSize
import tupperdate.android.home.NormalButtonSize
import tupperdate.android.ui.DislikeButton
import tupperdate.android.ui.LikeButton
import tupperdate.android.ui.RecipeAddButton
import tupperdate.android.ui.ReturnButton

@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onReturn: () -> Unit,
    onRecipeClick: () -> Unit,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
    {
        BarButton(
            size = LittleButtonSize,
            onClick = { onReturn() },
            backgroundColor = Color.ReturnButton,
            content = { Icon(vectorResource(id = R.drawable.ic_return)) }
        )
        BarButton(
            size = NormalButtonSize,
            onClick = { onDislike() },
            backgroundColor = Color.DislikeButton,
            content = { Icon(vectorResource(id = R.drawable.ic_clear)) })
        BarButton(
            size = NormalButtonSize,
            onClick = { onLike() },
            backgroundColor = Color.LikeButton,
            content = { Icon(vectorResource(id = R.drawable.ic_like)) })
        BarButton(
            size = LittleButtonSize,
            onClick = { onRecipeClick() },
            backgroundColor = Color.RecipeAddButton,
            content = { Icon(vectorResource(id = R.drawable.ic_add_recipe)) })
    }
}