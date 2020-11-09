package tupperdate.android.home

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.unit.Dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.MainBottomBar
import tupperdate.android.appbars.TitleTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.RecipeApi
import tupperdate.api.api
import kotlin.math.absoluteValue

@Composable
fun Home(
    modifier: Modifier = Modifier,
    recipeApi: RecipeApi,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onReturnClick: () -> Unit,
    onRecipeClick: () -> Unit,
) {
    // TODO make a call to front element of recipeApi
    val presentRecipe = (RecipeApi.Recipe(
        "Red lobster",
        "Look at me, am I not yummy ?",
        "lobster.jpg"
    ))

    TitleTopBar(onChatClick = onChatClick, onProfileClick = onProfileClick)

    Row(modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth()
                .align(Alignment.Bottom)
                .padding(bottom = VeryLittleButtonSize)
        ) {
            DisplayRecipeCard(
                presentRecipe = presentRecipe,
                onLike = { recipeApi.like(presentRecipe) },
                onDislike = { recipeApi.dislike(presentRecipe) },
            )
            MainBottomBar(
                onLike = { recipeApi.like(presentRecipe) },
                onDislike = { recipeApi.like(presentRecipe) },
                onReturn = onReturnClick,
                onRecipeClick = onRecipeClick
            )
        }
    }
}

@Composable
private fun DisplayRecipeCard(
    modifier: Modifier = Modifier,
    presentRecipe: RecipeApi.Recipe,
    onLike: () -> Unit,
    onDislike: () -> Unit,
) {
    WithConstraints {
        //determine height and width of card according to constraints
        //the card measures 90 % of screen width and 75 % of screen height
        val boxHeight: Dp
        val boxWidth: Dp
        val defaultPos: Int

        with(DensityAmbient.current) {
            boxHeight = constraints.maxHeight.toDp() * 0.75f
            boxWidth = constraints.maxWidth.toDp() * 0.9f
            // WithConstraints forces us to define manually a default position and work with
            defaultPos = ((constraints.maxWidth.toDp() / 2) - (boxWidth / 2)).value.toInt()
        }

        val (pos, setPos) = remember { mutableStateOf((defaultPos)) }

        //the defaultPos needs a delta measured
        val (delta, setDelta) = remember { mutableStateOf(0) }
        val swipeMargin = (boxWidth / 2).value.toInt()

        Box(
            modifier = modifier
                .width((boxWidth))
                .height((boxHeight))
                .padding(bottom = (VeryLittleButtonSize * 0.8f))
                .draggable(Orientation.Horizontal,
                    onDragStarted = {},
                    onDrag =
                    { deltaTemp ->
                        run {
                            setPos(pos + (deltaTemp * 0.75).toInt())
                            setDelta(delta + deltaTemp.toInt())
                        }
                    },
                    onDragStopped =
                    {
                        if (delta.absoluteValue < swipeMargin) {
                            setPos(defaultPos)
                            setDelta(0)
                        }
                    })
        )
        {
            RecipeCard(offset = pos, boxWidth = boxWidth, recipe = presentRecipe)
            if (delta.absoluteValue > swipeMargin) {
                if (pos > defaultPos) {
                    onLike()
                } else if (pos < defaultPos) {
                    onDislike()
                }
                setPos(defaultPos)
                setDelta(0)
                //the like() call will change the presentRecipe
            }
        }
    }
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
