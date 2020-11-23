package tupperdate.android.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.Smurf500

/**
 * A bar that is displayed for navigation.
 *
 * @param onChatClick callback called when the chat destination is clicked
 * @param onProfileClick callback called when the profile destination is clicked
 * @param modifier the [Modifier] for this composable
 */
@Composable
fun ChatTopBar(
    onRecipeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .preferredHeight(TopBarHeight),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {

        Icon(vectorResource(R.drawable.ic_home_messages), tint = Color.Smurf500)
        IconButton(onClick = onRecipeClick) {
            Icon(asset = vectorResource(id = R.drawable.ic_chat_recipes))
        }
        IconButton(onClick = onProfileClick) {
            Icon(vectorResource(R.drawable.ic_home_accounts))
        }
    }
}

private val TopBarHeight = 54.dp
