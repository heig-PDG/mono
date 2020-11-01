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
import tupperdate.api.RealRecipeApi.dislike
import tupperdate.api.RecipeApi
import tupperdate.api.RealRecipeApi.like
import tupperdate.api.api
import kotlin.math.absoluteValue

var presentRecipe= getNewRecipe()

@Composable
fun Home(
    recipeApi: RecipeApi,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onReturn: () -> Unit,
    onRecipeClick: () -> Unit
) {
    Scaffold(
        topBar = { mainTopBar(onChatClick, onProfileClick) },
        bodyContent = { defaultContent() },
        bottomBar = { mainBottomBar({ like(presentRecipe) },
            { dislike(presentRecipe) }, onReturn, onRecipeClick) }
    )
}

@Composable
fun defaultContent(
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
                        like(presentRecipe)
                    } else if (pos < 0) {
                        dislike(presentRecipe)
                    }
                    setPos(0)
                    presentRecipe=(getNewRecipe())
                    recipeCard(pos, presentRecipe) // TODO : get a new recipe and pass it to the box
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
    val owner= LifecycleOwnerAmbient.current
    val api= remember { owner.api() }
    TupperdateTheme {
        Home(
            recipeApi = api.recipe,
            onChatClick = {},
            onProfileClick = {},
            onRecipeClick = {},
            onReturn = {}
        )
    }
}

const val boxContentWidth: Int = 300
const val boxContentHeight: Int = 525
const val swipeMargin: Int = 200
