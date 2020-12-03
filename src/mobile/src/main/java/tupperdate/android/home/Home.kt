package tupperdate.android.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import tupperdate.android.home.feed.Feed
import tupperdate.api.Api

@Composable
fun Home(
    api: Api,
    onReturnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (currentSection, setCurrentSection) = savedInstanceState { HomeSections.Feed }

    Scaffold(topBar = {
        TupperdateTopBar(
            onChatClick = { setCurrentSection(HomeSections.MessageList) },
            onProfileClick = { setCurrentSection(HomeSections.Profile) },
            onTitleClick = { setCurrentSection(HomeSections.Feed) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    ) { innerPadding ->
        val innerModifier = Modifier.padding(innerPadding)

        Crossfade(current = currentSection) { section ->
            when (section) {
                HomeSections.Feed -> Feed(
                    recipeApi = api.recipe,
                    onReturnClick = onReturnClick,
                    onRecipeClick = {},
                    onRecipeDetailsClick = {},
                    modifier = innerModifier,
                )
                HomeSections.MessageList -> MessageList()
                HomeSections.Profile -> Profile()
            }
        }
    }
}

@Composable
fun MessageList() {
    Text("Message List")
}

@Composable
fun Profile() {
    Text("Profile")
}

private enum class HomeSections(
) {
    MessageList,
    Feed,
    Profile,
}
