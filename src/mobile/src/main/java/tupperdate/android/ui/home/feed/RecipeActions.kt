package tupperdate.android.ui.home.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.DislikeButton
import tupperdate.android.ui.theme.LikeButton
import tupperdate.android.ui.theme.RecipeAddButton
import tupperdate.android.ui.theme.ReturnButton

@Composable
fun RecipeActions(
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onBackClick: () -> Unit,
    onNewRecipeClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(modifier, Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        IconButton(
            onClick = onBackClick,
            icon = vectorResource(R.drawable.ic_home_cancel),
            backgroundColor = Color.ReturnButton,
            enabled = enabled,
            modifier = Modifier.preferredSize(SmallSize)
        )
        IconButton(
            onClick = onDislikeClick,
            icon = vectorResource(R.drawable.ic_home_dislike_recipe),
            backgroundColor = Color.DislikeButton,
            enabled = enabled,
            modifier = Modifier.preferredSize(HugeSize)
        )
        IconButton(
            onClick = onLikeClick,
            icon = vectorResource(R.drawable.ic_home_like_recipe),
            backgroundColor = Color.LikeButton,
            enabled = enabled,
            modifier = Modifier.preferredSize(HugeSize)
        )
        IconButton(
            onClick = onNewRecipeClick,
            icon = vectorResource(R.drawable.ic_home_new_recipe),
            backgroundColor = Color.RecipeAddButton,
            modifier = Modifier.preferredSize(SmallSize)
        )
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        enabled = enabled,
    ) {
        Icon(icon)
    }
}

private val SmallSize = 48.dp
private val HugeSize = 56.dp
