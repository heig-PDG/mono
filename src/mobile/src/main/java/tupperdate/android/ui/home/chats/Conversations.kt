package tupperdate.android.ui.home.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.components.ProfilePicture

@Composable
fun Conversations(
    onRecipeClick: () -> Unit,
    onProfileClick: () -> Unit,
    recipes: List<Recipe>,
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
                items(recipes) {
                    RecipeImage(imageUrl = it.picture)
                }
            }
        }
        item {
            Header(stringResource(R.string.chat_conversations))
        }
        items(conversations) {
            Conversation(
                title = it.displayName,
                subtitle = "NOT SUPPORTED", // TODO : Implement this.
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

@Composable
private fun RecipeImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    ProfilePicture(
        modifier = modifier.size(56.dp),
        image = imageUrl ?: "", // TODO: Fix this default value for missing images
        highlighted = false
    )
}
