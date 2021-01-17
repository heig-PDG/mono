package tupperdate.android.ui.home.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.DislikeButton
import tupperdate.android.ui.theme.LikeButton
import tupperdate.android.ui.theme.layout.SwipeStack
import tupperdate.android.ui.theme.layout.SwipeStackValue.*
import tupperdate.android.ui.theme.layout.rememberSwipeStackState

@Composable
fun Feed(
    onNewRecipeClick: () -> Unit,
    onOpenRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<FeedViewModel>()
    val recipes by viewModel.stack().collectAsState(emptyList())
    val unswipeable by viewModel.unswipeEnabled.collectAsState(false)

    val warnAllergens by viewModel.warnIfHasAllergens.collectAsState(false)
    val warnNotVegetarian by viewModel.warnIfNotVegetarian.collectAsState(false)

    Feed(
        recipes = recipes,
        unswipeEnabled = unswipeable,

        // Warnings.
        warnAllergens = warnAllergens,
        warnNotVegetarian = warnNotVegetarian,

        // Recipe callbacks.
        onRecipeLiked = viewModel::onLike,
        onRecipeDisliked = viewModel::onDislike,
        onRecipePreviousClick = viewModel::onUnswipe,

        // External interactions.
        onNewRecipeClick = onNewRecipeClick,
        onRecipeDetailsClick = onOpenRecipeClick,
        modifier = modifier,
    )
}

/**
 * Decides which warning to display to the user, if relevant.
 *
 * @param allergens if the allergens warning should be displayed.
 * @param vegetarian if the vegetarian warning should be displayed.
 */
@Composable
private fun warningText(
    allergens: Boolean,
    vegetarian: Boolean,
): String? = when {
    allergens && vegetarian -> stringResource(R.string.warn_all)
    allergens -> stringResource(R.string.warn_has_allergens)
    vegetarian -> stringResource(R.string.warn_not_vegetarian)
    else -> null
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Feed(
    recipes: List<Recipe>,
    unswipeEnabled: Boolean,
    // Warnings
    warnAllergens: Boolean,
    warnNotVegetarian: Boolean,
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
        // Eventually improve the SwipeStack API to avoid onCommit usages.
        val state = rememberSwipeStackState()

        // Use the top recipe as a sentinel as to whether we should snap back to the not swiped
        // state.
        val topRecipe = recipes.firstOrNull()
        onCommit(topRecipe) {
            if (topRecipe != null && state.isSwiped) state.snapTo(NotSwiped)
        }

        // Change state on recomposition.
        val anchor = state.value
        onCommit(anchor) {
            when (anchor) {
                NotSwiped -> Unit // Ignored.
                SwipedStart -> onRecipeDisliked()
                SwipedEnd -> onRecipeLiked()
            }
        }

        Box(Modifier.weight(1f)) {
            EmptyFeed(Modifier.fillMaxSize().padding(32.dp))
            SwipeStack(
                items = recipes,
                modifier = Modifier.fillMaxSize(),
                swipeStackState = state,
            ) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onInfoClick = { onRecipeDetailsClick(recipe) },
                    modifier = Modifier.fillMaxSize(),
                    warning = warningText(
                        allergens = warnAllergens && recipe.attributes.hasAllergens,
                        vegetarian = warnNotVegetarian && !recipe.attributes.vegetarian,
                    ),
                    overlay = {
                        val progress = state.progress
                        // Swiping to the "like" state.
                        if (progress.from == NotSwiped
                            && progress.to == SwipedEnd
                            && topRecipe == recipe
                        ) {
                            SwipeOverlay(
                                color = Color.LikeButton,
                                progress = state.progress.fraction,
                                vector = vectorResource(R.drawable.ic_home_like_recipe),
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        // Swiping to the "dislike" state.
                        if (progress.from == NotSwiped
                            && progress.to == SwipedStart
                            && topRecipe == recipe
                        ) {
                            SwipeOverlay(
                                color = Color.DislikeButton,
                                progress = state.progress.fraction,
                                vector = vectorResource(R.drawable.ic_home_dislike_recipe),
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                )
            }
        }

        RecipeActions(
            onLikeClick = state::swipeEnd,
            onDislikeClick = state::swipeStart,
            onBackClick = onRecipePreviousClick,
            onNewRecipeClick = onNewRecipeClick,
            modifier = Modifier.fillMaxWidth(),
            likeEnabled = topRecipe != null,
            unswipeEnabled = unswipeEnabled,
        )
    }
}
