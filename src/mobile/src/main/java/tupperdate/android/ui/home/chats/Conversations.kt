package tupperdate.android.ui.home.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.data.features.messages.Conversation
import tupperdate.android.data.features.messages.Match

@Composable
fun Conversations(
    onRecipeClick: () -> Unit,
    onProfileClick: () -> Unit,
    matches: List<Match>,
    conversations: List<Conversation>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        item {
            Header(stringResource(id = R.string.chat_matches_list))
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(matches) {
                    MatchBubble(
                        pictures = it.theirPictures,
                        onClick = { /*TODO*/ },
                    )
                }
            }
        }
        item {
            Header(stringResource(R.string.chat_conversations))
        }
        items(conversations) {
            // TODO : Sort items by last timestamp in ViewModel.
            Conversation(
                title = it.previewTitle,
                subtitle = it.previewBody,
                highlighted = false, // TODO : Implement this.
                image = it.picture,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {})
                    .padding(8.dp),
            )
        }
    }
}

@Composable
private fun Header(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(title, modifier.padding(16.dp), style = MaterialTheme.typography.overline)
}
