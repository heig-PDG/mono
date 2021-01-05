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
import tupperdate.android.ui.theme.layout.SwipeStackValue.*
import tupperdate.android.ui.theme.layout.rememberSwipeStackState

@Composable
fun Feed(
    onNewRecipeClick: () -> Unit,
    onOpenRecipeClick: (Recipe) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<FeedViewModel>()
    val recipes by viewModel.stack().collectAsState(emptyList())

    Feed(
        recipes = recipes,
        onRecipeLiked = viewModel::onLike,
        onRecipeDisliked = viewModel::onDislike,
        onRecipePreviousClick = onBack,

        // Interactions
        onNewRecipeClick = onNewRecipeClick,
        onRecipeDetailsClick = onOpenRecipeClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Feed(
    recipes: List<Recipe>,
    // Interacting with the current recipe.
    onRecipeLiked: () -> Unit,
    onRecipeDisliked: () -> Unit,
    onRecipePreviousClick: () -> Unit,
    // Reading details or creating new recipes.
    onNewRecipeClick: () -> Unit,
    onRecipeDetailsClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        modifier = modifier.padding(16.dp)
    ) {
        // TODO (alex) : We should not rely on recomposition. Eventually change SwipeStack API.
        val state = rememberSwipeStackState()
        when (state.value) {
            NotSwiped -> Unit
            SwipedStart -> {
                onRecipeDisliked()
                state.snapTo(NotSwiped)
            }
            SwipedEnd -> {
                onRecipeLiked()
                state.snapTo(NotSwiped)
            }
        }

        SwipeStack(
            items = recipes,
            modifier = Modifier.weight(1F),
            // For the moment, reject all swipes.
            swipeStackState = state,
        ) { recipe ->
            RecipeCard(
                recipe = recipe,
                onInfoClick = { onRecipeDetailsClick(recipe) },
                modifier = Modifier.fillMaxSize()
            )
        }

        RecipeActions(
            onLikeClick = state::swipeEnd,
            onDislikeClick = state::swipeStart,
            onBackClick = onRecipePreviousClick,
            onNewRecipeClick = onNewRecipeClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
