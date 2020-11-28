package tupperdate.android.chats

import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.RecipeApi
import tupperdate.android.R
import tupperdate.android.appbars.TupperdateTopBar
import tupperdate.android.ui.components.ProfilePicture

@Composable
fun ConversationsPage(
    onRecipeClick: () -> Unit,
    onProfileClick: () -> Unit,
    recipes: List<RecipeApi.Recipe>,
    conversations: List<Conversation>,
    modifier: Modifier = Modifier
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TupperdateTopBar(
            //TODO modify the TupperdateTopBar
            onChatClick = onRecipeClick,
            onTitleClick = {},
            onProfileClick = onProfileClick,
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.padding(top = 26.dp, bottom = 16.dp)) {
            Text(
                text = stringResource(id = R.string.chat_matches_list),
                style = MaterialTheme.typography.overline
            )
        }
        Matches(recipes = recipes, modifier)
        Row(Modifier.padding(top = 16.dp, bottom = 24.dp)) {
            Text(
                text = stringResource(id = R.string.chat_conversations),
                style = MaterialTheme.typography.overline
            )
        }
        Chats(conversations = conversations, onConversationClick = {})
    }
}

@Composable
private fun Matches(
    recipes: List<RecipeApi.Recipe>,
    modifier: Modifier = Modifier
) {
    LazyRowFor(items = recipes) {
        RecipeImage(imageUrl = it.pictureUrl, modifier)
    }
}

@Composable
private fun RecipeImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    ProfilePicture(
        modifier = modifier.size(56.dp),
        image = imageUrl,
        highlighted = false
    )
}

@Preview
@Composable
fun ConversationsPagePreview() {
    var recipes = listOf(
        RecipeApi.Recipe(
            title = "Lobster",
            description = "From Santa Monica",
            pictureUrl = "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg"
        )
    )
    var conv = listOf(
        Conversation(
            id = "12e",
            title = "Aloy",
            subtitle = "Sorry, not today !",
            highlighted = true,
            image = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg"
        ),
        Conversation(
            id = "12d",
            title = "Ciri",
            subtitle = "Know when fairy tales cease to be tales ? When people start believing in them.",
            highlighted = false,
            image = "https://static.wikia.nocookie.net/sorceleur/images/8/81/Ciri.png/revision/latest?cb=20140902222834",
        ),
        Conversation(
            id = "12f",
            title = "The Mandalorian",
            subtitle = "This is the way.",
            highlighted = true,
            image = "https://imgix.bustle.com/uploads/image/2020/11/19/d66dd1be-49fc-46f6-b9fd-a40f20304b74-102419_disney-the-mandalorian-00-1-780x440-1572307750.jpg"
        ),
    )
    TupperdateTheme {
        ConversationsPage(
            onRecipeClick = {},
            onProfileClick = {},
            recipes = recipes,
            conversations = conv)
    }
}
