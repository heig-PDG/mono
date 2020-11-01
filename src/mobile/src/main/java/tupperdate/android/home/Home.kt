package tupperdate.android.home

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.mainBottomBar
import tupperdate.android.appbars.mainTopBar
import tupperdate.android.ui.TupperdateTheme
import kotlin.math.absoluteValue
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTypography
import tupperdate.api.RealRecipeApi.dislike
import tupperdate.api.RealRecipeApi.like
import tupperdate.api.RecipeApi

var presentRecipe= getNewRecipe()

@Composable
fun Home(
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
    TupperdateTheme {
        Home(
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
