package tupperdate.android.home

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onDispose
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.mainBottomBar
import tupperdate.android.appbars.mainTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.AuthenticationApi
import tupperdate.api.RealRecipeApi.dislike
import tupperdate.api.RealRecipeApi.like
import kotlin.math.absoluteValue
import tupperdate.android.R

@Composable
fun Home(
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onReturn: () -> Unit,
    onRecipeClick: () -> Unit
) {
    Scaffold(
        topBar = { mainTopBar(onChatClick, onProfileClick) },
        bodyContent = { defaultContent(onLike, onDislike) },
        bottomBar = { mainBottomBar(onLike, onDislike, onReturn, onRecipeClick) }
    )
}

@Composable
fun blueBox(offset: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.size(boxContentSize.dp).offset(x = offset.dp)
            // TODO find new parameter name for shape
            //shape = RoundedCornerShape(8.dp),
            .background(Color.Cyan), alignment = Alignment.Center
    ) {
        Image(
            imageResource(id = R.drawable.redlobtser),
            Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Text(text = "Red lobster")
    }
}

@Composable
fun defaultContent(
    onLike: () -> Unit,
    onDislike: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().align(Alignment.CenterVertically)) {
            val (pos, setPos) = remember { mutableStateOf(0) }
            Box(modifier = Modifier.size(boxContentSize.dp).align(Alignment.CenterHorizontally)
                .draggable(Orientation.Horizontal,
                    onDragStarted = {
                        //Toast.makeText(applicationContext, "It really works", Toast.LENGTH_SHORT).show()
                    }, onDrag =
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
                    blueBox(pos)
                } else {
                    blueBox(pos)
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
            onLike = {},
            onDislike = {},
            onRecipeClick = {},
            onReturn = {}
        )
    }
}

const val TAG: String = "Main"
const val boxContentSize: Int = 300
const val swipeMargin: Int = 200
