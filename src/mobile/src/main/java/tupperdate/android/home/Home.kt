package tupperdate.android.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import tupperdate.android.home.chats.Conversations
import tupperdate.android.home.feed.Feed
import tupperdate.android.home.profile.Profile
import tupperdate.api.Api

@Composable
fun Home(
    api: Api,
    onReturnClick: () -> Unit,
    onDevClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val profile = remember { api.users.profile }.collectAsState(initial = null).value
    val (currentSection, setCurrentSection) = savedInstanceState { HomeSections.Feed }

    Scaffold(topBar = {
        TupperdateTopBar(
            onChatClick = { setCurrentSection(HomeSections.Conversations) },
            onProfileClick = { setCurrentSection(HomeSections.Profile) },
            onTitleClick = { setCurrentSection(HomeSections.Feed) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    ) { innerPadding ->
        val innerModifier = modifier.padding(innerPadding)

        Crossfade(current = currentSection) { section ->
            when (section) {
                HomeSections.Feed -> Feed(
                    recipeApi = api.recipe,
                    onReturnClick = onReturnClick,
                    onRecipeClick = {},
                    onRecipeDetailsClick = {},
                    modifier = innerModifier,
                )
                HomeSections.Conversations -> Conversations(
                    onRecipeClick = {},
                    onProfileClick = {},
                    recipes = listOf(),
                    conversations = listOf(),
                    modifier = innerModifier,
                )
                HomeSections.Profile -> Profile(
                    userApi = api.users,
                    imagePicker = api.images,
                    profile = profile ?: api.users.emptyProfile,
                    onCloseClick = { setCurrentSection(HomeSections.Feed) },
                    onSignOutClick = {},
                    onDevClick = onDevClick,
                    modifier = innerModifier,
                )
            }
        }
    }
}

private enum class HomeSections(
) {
    Conversations,
    Feed,
    Profile,
}
