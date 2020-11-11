package tupperdate.android.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.TupperdateTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.layout.SwipeStack
import tupperdate.android.ui.layout.rememberSwipeStackState
import tupperdate.api.RecipeApi
import tupperdate.api.api

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    recipeApi: RecipeApi,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onReturnClick: () -> Unit,
    onRecipeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val recipes by remember { recipeApi.stack() }.collectAsState(emptyList())
    Scaffold(
        topBar = {
            TupperdateTopBar(
                onChatClick = onChatClick,
                onProfileClick = onProfileClick,
                Modifier.fillMaxWidth()
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
                    onInfoClick = {},
                    Modifier.fillMaxSize()
                )
            }
        },
        bottomBar = {
            SwipeCardBottomBar(
                onLike = { /* TODO */ },
                onDislike = { /* TODO */ },
                onReturn = onReturnClick,
                onRecipeClick = onRecipeClick
            )
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun HomeDisconnectPreview() {
    val owner = LifecycleOwnerAmbient.current
    val api = remember { owner.api() }
    TupperdateTheme {
        Home(
            recipeApi = api.recipe,
            onChatClick = {},
            onProfileClick = {},
            onRecipeClick = {},
            onReturnClick = {}
        )
    }
}
