package tupperdate.android.home

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import io.ktor.client.engine.*
import tupperdate.android.appbars.mainBottomBar
import tupperdate.android.appbars.mainTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.RecipeApi
import tupperdate.api.api
import kotlin.math.absoluteValue

@Composable
/*
@ Guy-Laurent : I am forced to do only one function because it is the only way
to call like() and dislike() without the weird RealRecipeApi import.
 */
fun Home(
    recipeApi: RecipeApi,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onReturnClick: () -> Unit,
    onRecipeClick: () -> Unit
) {
    // TODO make a call to front element of recipeApi
    val presentRecipe = (RecipeApi.Recipe(
        "Red lobster",
        "Look at me, am I not yummy ?",
        "lobster.jpg"
    ))
    mainTopBar(onChatClick = onChatClick, onProfileClick = onProfileClick)

    DisplayRecipeCard(
        presentRecipe = presentRecipe,
        onLike = { recipeApi.like(presentRecipe) },
        onDislike = { recipeApi.dislike(presentRecipe) },
        onReturnClick = onReturnClick,
        onRecipeClick = onRecipeClick
    )
}

@Composable
private fun DisplayRecipeCard(
    presentRecipe: RecipeApi.Recipe,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onReturnClick: () -> Unit,
    onRecipeClick: () -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth().align(Alignment.Bottom)
                .padding(bottom = veryLittleButtonSize.dp)
        ) {
            WithConstraints { -> //why no parameter here ??
                //determine height and width of card according to constraints
                //the card measures 90 % of screen width and 75 % of screen height
                val boxHeight =
                    with(DensityAmbient.current) { constraints.maxHeight.toDp() * 0.75f }
                val boxWidth = with(DensityAmbient.current) { constraints.maxWidth.toDp() * 0.9f }

                //the align(..) doesn't work anymore (because of reasons), so we need to
                //set manually a default pos
                val defaultPos = ((with(DensityAmbient.current) { constraints.maxWidth.toDp() / 2 })
                        - boxWidth / 2).value.toInt()
                val (pos, setPos) = remember { mutableStateOf((defaultPos)) }

                //the defaultPos needs a delta measured
                val (delta, setDelta) = remember { mutableStateOf(0) }
                val swipeMargin = (boxWidth / 2).value.toInt()

                Box(modifier = Modifier.align(Alignment.CenterHorizontally)
                    .width((boxWidth)).height((boxHeight))
                    .padding(bottom = (veryLittleButtonSize * 0.8f).dp)
                    .draggable(Orientation.Horizontal,
                        onDragStarted = {}, onDrag =
                        { deltaTemp ->
                            run {
                                setPos(pos + (deltaTemp * 0.75).toInt())
                                setDelta(delta+deltaTemp.toInt())
                            }
                        }, onDragStopped = {
                            if (delta.absoluteValue < swipeMargin) {
                                setPos(defaultPos)
                                setDelta(0)
                            }
                        })
                )
                {
                    if (delta.absoluteValue > swipeMargin) {
                        if (pos > defaultPos) {
                            onLike()
                        } else if (pos < defaultPos) {
                            onDislike()
                        }
                        setPos(defaultPos)
                        setDelta(0)
                        //the like() call will change the presentRecipe
                        recipeCard(pos, presentRecipe, boxWidth)
                    } else {
                        recipeCard(pos, presentRecipe, boxWidth)
                    }
                }
            }

            mainBottomBar(
                onLike = onLike,
                onDislike = onDislike,
                onReturn = onReturnClick,
                onRecipeClick = onRecipeClick
            )
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
