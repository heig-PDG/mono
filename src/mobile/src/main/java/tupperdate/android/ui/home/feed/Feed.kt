package tupperdate.android.ui.home.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.layout.SwipeStack
import tupperdate.android.ui.theme.layout.rememberSwipeStackState

@Composable
fun Feed(
    onBack: () -> Unit,
    onOpenRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<FeedViewModel>()
    val recipes by viewModel.stack().collectAsState(emptyList())

    Feed(
        recipes = recipes,
        onBack = onBack,
        onRecipeClick = { /* TODO */ },
        onRecipeDetailsClick = onOpenRecipeClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Feed(
    recipes: List<Recipe>,
    onBack: () -> Unit,
    onRecipeClick: () -> Unit,
    onRecipeDetailsClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
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
