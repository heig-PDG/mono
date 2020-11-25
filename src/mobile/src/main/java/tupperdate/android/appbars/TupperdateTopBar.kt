package tupperdate.android.appbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.Flamingo500
import tupperdate.android.ui.Smurf500
import tupperdate.android.ui.TupperdateTypography

/**
 * A bar that is displayed for navigation.
 *
 * @param onChatClick callback called when the chat destination is clicked
 * @param onProfileClick callback called when the profile destination is clicked
 * @param modifier the [Modifier] for this composable
 */
@Composable
fun TupperdateTopBar(
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .padding(horizontal = TopBarPadding)
            .preferredHeight(TopBarHeight),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {
        Providers(AmbientContentColor provides TopBarIconColor) {
            IconButton(onClick = onChatClick) {
                // TODO change icon when notified
                Icon(vectorResource(R.drawable.ic_home_messages))
            }

            Text(
                text = Title,
                style = TupperdateTypography.h6,
                modifier = Modifier.multiClick(
                    triggerAmount = 5,
                    action = onTitleClick,
                )
            )

            IconButton(onClick = onProfileClick) {
                Icon(vectorResource(R.drawable.ic_home_accounts))
            }
        }
    }
}

/**
 * Use an [AnnotatedString], which can have some additional styling for substrings. Its builder
 * uses a stack-based API to add and remove styles, which are applied as content is appended.
 */
private val Title = annotatedString {
    withStyle(SpanStyle(color = Color.Flamingo500)) { append("tupper ") }
    withStyle(SpanStyle(color = Color.Smurf500)) { append("• date") }
}

private val TopBarHeight = 54.dp
private val TopBarPadding = 8.dp
private val TopBarIconColor = Color.Black.copy(alpha = .6f) // TODO : Move to color palette.

private fun Modifier.multiClick(triggerAmount: Int, action: () -> Unit) = composed {
    val (count, setCount) = remember { mutableStateOf(0) }
    if (count < triggerAmount) {
        this then clickable(onClick = { setCount(count + 1) })
    } else {
        this then clickable(onClick = action)
    }
}
