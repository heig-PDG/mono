package tupperdate.android.ui.home.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme

@Composable
fun RecipeInfoButton(
    onClick: () -> Unit,
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val emphasis = when {
        enabled -> ContentAlpha.medium
        else -> ContentAlpha.disabled
    }
    Providers(AmbientContentAlpha provides emphasis) {
        Column(
            modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                    indication = rememberRipple()
                )
                .padding(16.dp),
            Arrangement.spacedBy(8.dp),
            Alignment.CenterHorizontally,
        ) {
            Icon(icon)
            Text(title, style = MaterialTheme.typography.overline)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeInfoPreviewEnabled() {
    TupperdateTheme {
        RecipeInfoButton(
            onClick = {},
            icon = vectorResource(R.drawable.ic_editrecipe_veggie),
            title = "NOT VEGAN", //not put in strings.xml because it is previews
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeInfoPreviewDisabled() {
    TupperdateTheme {
        RecipeInfoButton(
            onClick = {},
            icon = vectorResource(R.drawable.ic_editrecipe_not_veggie),
            title = "VEGAN",
            enabled = false,
        )
    }
}
