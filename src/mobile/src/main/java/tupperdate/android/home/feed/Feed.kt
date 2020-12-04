package tupperdate.android.home.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.layout.SwipeStack
import tupperdate.android.ui.layout.rememberSwipeStackState
import tupperdate.api.RecipeApi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Feed(
    recipeApi: RecipeApi,
    onBack: () -> Unit,
    onRecipeClick: () -> Unit,
    onRecipeDetailsClick: (RecipeApi.Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    val recipes by remember { recipeApi.stack() }.collectAsState(emptyList())

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        modifier = modifier.padding(16.dp)
    ) {
        SwipeStack(
            items = recipes,
            modifier = Modifier.weight(1F),
            // For the moment, reject all swipes.
            swipeStackState = rememberSwipeStackState(confirmStateChange = { false })
        ) { recipe ->
            RecipeCard(
                recipe = recipe,
                onInfoClick = { onRecipeDetailsClick(recipe) },
                modifier = Modifier.fillMaxSize()
            )
        }

        RecipeActions(
            onLikeClick = { /* TODO */ },
            onDislikeClick = { /* TODO */ },
            onBackClick = onBack,
            onNewRecipeClick = onRecipeClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }

}
