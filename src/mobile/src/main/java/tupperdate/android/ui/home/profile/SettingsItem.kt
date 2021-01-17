package tupperdate.android.ui.home.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R

/**
 * A composable that displays an item in the application settings. Items can be used to open
 * dedicated dialogs, pickers, or be expanded with more content.
 *
 * @param icon the icon for this item
 * @param title the title for this item
 * @param modifier the modifier for this composable
 *
 * @see [ExpandableSettingsItem] an expandable variant of [SettingsItem]
 */
@Composable
fun SettingsItem(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier.padding(16.dp),
        Arrangement.spacedBy(16.dp),
        Alignment.CenterVertically,
    ) {
        icon()
        ProvideTextStyle(MaterialTheme.typography.body1) {
            title()
        }
        Spacer(Modifier.weight(1f, true))
        if (actions != null) {
            actions()
        }
    }
}

/**
 * An alternative to [SettingsItem] that provides an expandable body.
 *
 * @param icon the icon for this item
 * @param title the title for this item
 * @param modifier the modifier for this composable
 * @param content the content to display on expansion
 */
@Composable
@OptIn(ExperimentalAnimationApi::class)
fun ExpandableSettingsItem(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val (open, setOpen) = remember { mutableStateOf(false) }
    Column(modifier.clickable(onClick = { setOpen(!open) })) {
        val angle = animate(if (open) 90f else 0f)
        SettingsItem(
            icon = icon,
            title = title,
            actions = {
                Icon(
                    vectorResource(R.drawable.ic_profile_expand),
                    Modifier.rotate(angle),
                )
            },
        )
        AnimatedVisibility(open) {
            Box { content() }
        }
    }
}
