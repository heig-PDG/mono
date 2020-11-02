package tupperdate.android.home

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
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
    Scaffold(
        topBar = { mainTopBar(onChatClick, onProfileClick) },
        bodyContent = {
            defaultContent(presentRecipe, { recipeApi.like(presentRecipe) },
                { recipeApi.dislike(presentRecipe) })
        },
        bottomBar = {
            mainBottomBar(
                { recipeApi.like(presentRecipe) },
                { recipeApi.dislike(presentRecipe) }, onReturnClick, onRecipeClick
            )
        }
    )
}

@Composable
private fun defaultContent(
    presentRecipe: RecipeApi.Recipe,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().align(Alignment.CenterVertically)) {
            val (pos, setPos) = remember { mutableStateOf(0) }
            Box(modifier = Modifier.width(boxContentWidth.dp).height(boxContentHeight.dp)
                .align(Alignment.CenterHorizontally)
                .draggable(Orientation.Horizontal,
                    onDragStarted = {}, onDrag =
                    { delta ->
                        run {
                            setPos(pos + (delta * 0.75).toInt())
                        }
                    }, onDragStopped = { if (pos.absoluteValue < swipeMargin) setPos(0) })
            )
            {
                if (pos.absoluteValue > swipeMargin) {
                    if (pos > 0) {
                        onLike
                    } else if (pos < 0) {
                        onDislike
                    }
                    setPos(0)
                    //the like() call will change the presentRecipe
                    recipeCard(pos, presentRecipe)
                } else {
                    recipeCard(pos, presentRecipe)
                }
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

const val boxContentWidth: Int = 300
const val boxContentHeight: Int = 383
const val swipeMargin: Int = 200
