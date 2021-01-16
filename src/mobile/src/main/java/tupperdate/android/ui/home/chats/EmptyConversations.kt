package tupperdate.android.ui.home.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme

@Composable
fun EmptyConversations(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier,
        Arrangement.spacedBy(64.dp, Alignment.CenterVertically),
        Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = vectorResource(R.drawable.empty_chat_screen),
            contentScale = ContentScale.Inside,
        )
        Providers(AmbientContentAlpha provides ContentAlpha.disabled) {
            Text(
                text = stringResource(R.string.home_no_recipes),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun EmptyConversationsPreview() = TupperdateTheme {
    EmptyConversations()
}
