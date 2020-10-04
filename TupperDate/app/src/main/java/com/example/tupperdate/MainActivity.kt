package com.example.tupperdate

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animatedFloat
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tupperdate.ui.TupperDateTheme
import java.util.*
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TupperDateTheme {
                Scaffold(
                    topBar = {
                        mainTopBar()
                    },
                    bodyContent = {
                        defaultContent()
                    },
                    bottomBar = {
                        mainBottomBar()
                    }
                )
            }
        }
    }

    @Composable
    fun blueBox(offset: Int) {
        Box(modifier = Modifier.size(boxContentSize.dp).offset(x = offset.dp), shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.Cyan, gravity = Alignment.Center) {
            Image(imageResource(R.drawable.redlobtser), Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
            Text(text = "Red lobster")
        }
    }

    @Composable
    fun defaultContent() {
        Row(Modifier.fillMaxSize(), verticalGravity = Alignment.CenterVertically) {
            Column(Modifier.fillMaxWidth(), horizontalGravity = Alignment.CenterHorizontally) {
                val (pos, setPos) = remember { mutableStateOf(0) }
                Box(modifier = Modifier.size(boxContentSize.dp)
                        .draggable(Orientation.Horizontal,
                                onDragStarted = {
                                    //Toast.makeText(applicationContext, "It really works", Toast.LENGTH_SHORT).show()
                                }, onDrag =
                        { delta ->
                            run {
                                setPos(pos + (delta * 0.75).toInt())
                            }
                        }, onDragStopped = { if (pos.absoluteValue < swipeMargin) setPos(0) }))
                {
                    if (pos.absoluteValue > swipeMargin) {
                        if (pos >0 ) like()
                        else if (pos < 0) dislike()
                        setPos(0)
                        blueBox(pos)
                    } else {
                        blueBox(pos)
                    }
                }
            }
        }
    }

    @Composable
    fun mainTopBar() {
        TopAppBar(
                title = {
                    Column(modifier = Modifier.weight(1f), horizontalGravity = Alignment.CenterHorizontally) {
                        Text(text = "tupper.date", textAlign = TextAlign.Center)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(vectorResource(id = R.drawable.ic_chat_24px))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(vectorResource(id = R.drawable.ic_account_circle_black_18dp))
                    }
                },
                backgroundColor = Color.White
        )
    }

    @Composable
    fun mainBottomBar() {
        BottomAppBar(backgroundColor = Color.White)
        {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
            {
                IconButton(onClick = {}) {
                    Icon(vectorResource(id = R.drawable.ic_navigate_before_black_18dp))
                }
                IconButton(onClick = {dislike()}) {
                    Icon(vectorResource(id = R.drawable.ic_clear_black_18dp))
                }
                IconButton(onClick = {like()}) {
                    Icon(vectorResource(id = R.drawable.ic_favorite_border_black_18dp))
                }
                IconButton(onClick = {}) {
                    Icon(vectorResource(id = R.drawable.ic_fastfood_black_18dp))
                }
            }
        }
    }

    private fun like()
    {
        Log.d(TAG, "Like")
    }

    private fun dislike()
    {
        Log.d(TAG, "Disike")
    }

    companion object {
        const val TAG: String = "Main"
        const val boxContentSize: Int = 300
        const val swipeMargin: Int = 200
    }
}
