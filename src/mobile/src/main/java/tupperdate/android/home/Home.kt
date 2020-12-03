package tupperdate.android.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.TupperdateTopBar
import tupperdate.android.ui.layout.SwipeStack
import tupperdate.android.ui.layout.rememberSwipeStackState
import tupperdate.api.RecipeApi

@Composable
fun Home(
) {
    val (currentSection, setCurrentSection) = savedInstanceState { HomeSections.Feed }

    Scaffold(topBar = {
        TupperdateTopBar(
            onChatClick = {},
            onProfileClick = {},
            onTitleClick = {}
        )
    }
    ) { innerPadding ->
        Crossfade(current = currentSection) { section ->
            when(section) {
                HomeSections.Feed -> Feed()
                HomeSections.MessageList -> MessageList()
                HomeSections.Profile -> Profile()
            }

        }

    }

}

@Composable
fun Feed() {
    Text("Feed")
}

@Composable
fun MessageList() {
    Text("Message List")
}

@Composable
fun Profile() {
    Text("Profile")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    recipeApi: RecipeApi,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onReturnClick: () -> Unit,
    onRecipeClick: () -> Unit,
    onRecipeDetailsClick: (RecipeApi.Recipe) -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val recipes by remember { recipeApi.stack() }.collectAsState(emptyList())
    Scaffold(
        topBar = {
            TupperdateTopBar(
                onChatClick = onChatClick,
                onProfileClick = onProfileClick,
                onTitleClick = onTitleClick,
                modifier = Modifier.fillMaxWidth()
            )
        },
        bodyContent = { paddingValues ->
            SwipeStack(
                recipes,
                Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                // For the moment, reject all swipes.
                swipeStackState = rememberSwipeStackState(confirmStateChange = { false })
            ) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onInfoClick = { onRecipeDetailsClick(recipe) },
                    Modifier.fillMaxSize()
                )
            }
        },
        bottomBar = {
            RecipeActions(
                onLikeClick = { /* TODO */ },
                onDislikeClick = { /* TODO */ },
                onBackClick = onReturnClick,
                onNewRecipeClick = onRecipeClick,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        modifier = modifier,
    )
}

private enum class HomeSections(
) {
    MessageList,
    Feed,
    Profile,
}

@Preview
@Composable
private fun HomeDisconnectPreview() {
    // TODO : Provide
    // val owner = LifecycleOwnerAmbient.current
    // val api = remember { owner.api() }
    // TupperdateTheme {
    //     Home(
    //         recipeApi = api.recipe,
    //         onChatClick = {},
    //         onProfileClick = {},
    //         onRecipeClick = {},
    //         onReturnClick = {},
    //         onRecipeDetailsClick = {},
    //     )
    // }
}
