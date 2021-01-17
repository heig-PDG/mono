package tupperdate.android.ui.home.profile

import androidx.compose.animation.animate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R

@Composable
fun Restriction(
    restricted: Boolean,
    onRestrictedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        item {
            RestrictionOption(
                onClick = { onRestrictedChange(true) },
                title = stringResource(R.string.profile_restrictions_enabled),
                selected = restricted,
            )
        }
        item {
            RestrictionOption(
                onClick = { onRestrictedChange(false) },
                title = stringResource(R.string.profile_restrictions_disabled),
                selected = !restricted,
            )
        }
    }
}

@Composable
private fun RestrictionOption(
    onClick: () -> Unit,
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val background = animate(
        if (selected) MaterialTheme.colors.primary.copy(alpha = 0.2f)
        else Color.Transparent
    )
    val colors = ButtonDefaults.textButtonColors(backgroundColor = background)
    OutlinedButton(onClick = onClick, colors = colors, modifier = modifier) {
        Text(title)
    }
}
