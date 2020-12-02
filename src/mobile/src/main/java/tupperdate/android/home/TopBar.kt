package tupperdate.android.home

import androidx.compose.foundation.AmbientIndication
import androidx.compose.foundation.Indication
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import tupperdate.android.home.TopBarDestination.*
import tupperdate.android.ui.Flamingo500
import tupperdate.android.ui.Smurf500
import tupperdate.android.ui.TupperdateTypography

enum class TopBarDestination {
    Chats,
    Recipes,
    Profile,
}

/**
 * A bar that is displayed for navigation.
 *
 * @param destination the currently selected [TopBarDestination]
 * @param onDestinationClick when a [TopBarDestination] is selected
 * @param modifier the [Modifier] for this composable
 */
@Composable
fun TopBar(
    destination: TopBarDestination,
    onDestinationClick: (TopBarDestination) -> Unit,
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
            IconButton(onClick = { onDestinationClick(Chats) }) {
                // TODO change icon when notified
                Icon(vectorResource(R.drawable.ic_home_messages))
            }

            Text(
                text = Title,
                style = TupperdateTypography.h6,
                modifier = Modifier
                    .multiClickable(
                        period = 2500, // ms
                        onClick = {
                            if (it == 5)
                                onDestinationClick(Recipes)
                        },
                        indication = null,
                    )
            )

            IconButton(onClick = { onDestinationClick(Profile) }) {
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

@Composable
private fun Modifier.multiClickable(
    period: Long,
    interactionState: InteractionState = remember { InteractionState() },
    indication: Indication? = AmbientIndication.current(),
    onClick: (Int) -> Unit,
) = composed {

    val (clicks, setClicks) = remember { mutableStateOf(emptyList<Long>()) }

    this then clickable(
        onClick = {
            val now = System.currentTimeMillis()
            val count = clicks.indexOfLast { it < now - period }
            val updated = clicks.drop(count + 1) + now
            setClicks(updated)
            onClick(updated.size)
        },
        interactionState = interactionState,
        indication = indication,
    )
}
