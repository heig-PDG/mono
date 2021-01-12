package tupperdate.android.ui.home.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.Smurf100

@Composable
fun ChatBottomBar(
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Smurf100)
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(end = 10.dp)
                .height(24.dp).weight(1f),
            value = "",
            onValueChange = {},
            placeholder = { stringResource(id = R.string.chat_message_placeholder) }
        )
        IconButton(onClick = onSendClick) {
            Icon(
                imageVector = vectorResource(id = R.drawable.ic_chat_send),
                tint = Color.Black
            )
        }
    }
}

private val TopBarHeight = 66.dp
