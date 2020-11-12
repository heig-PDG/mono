package tupperdate.android.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.DislikeButton
import tupperdate.android.ui.LikeButton
import tupperdate.android.ui.RecipeAddButton
import tupperdate.android.ui.ReturnButton

@Composable
fun RecipeActions(
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onBackClick: () -> Unit,
    onNewRecipeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier, Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        IconButton(
            onClick = onBackClick,
            icon = vectorResource(R.drawable.ic_return),
            backgroundColor = Color.ReturnButton,
            Modifier.preferredSize(SmallSize)
        )
        IconButton(
            onClick = onDislikeClick,
            icon = vectorResource(R.drawable.ic_clear),
            backgroundColor = Color.DislikeButton,
            Modifier.preferredSize(HugeSize)
        )
        IconButton(
            onClick = onLikeClick,
            icon = vectorResource(R.drawable.ic_like),
            backgroundColor = Color.LikeButton,
            Modifier.preferredSize(HugeSize)
        )
        IconButton(
            onClick = onNewRecipeClick,
            icon = vectorResource(R.drawable.ic_add_recipe),
            backgroundColor = Color.RecipeAddButton,
            Modifier.preferredSize(SmallSize)
        )
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    icon: VectorAsset,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier,
        colors = ButtonConstants.defaultButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Icon(icon)
    }
}

private val SmallSize = 48.dp
private val HugeSize = 56.dp
