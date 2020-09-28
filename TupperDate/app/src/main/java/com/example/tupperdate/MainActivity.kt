package com.example.tupperdate

import android.graphics.Paint
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.text.Layout
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.view.Gravity
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.tupperdate.ui.TupperDateTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TupperDateTheme {
                Scaffold(
                        topBar = {
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
                        },
                        bodyContent = {
                            Row(Modifier.fillMaxSize(), verticalGravity = Alignment.CenterVertically) {
                                Column() {
                                    Image(imageResource(R.drawable.redlobtser), Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
                                    Text(text = "Red lobster")
                                }
                            }
                        },
                        bottomBar = {
                            BottomAppBar(backgroundColor = Color.White)
                            {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                                {
                                    IconButton(onClick = {}) {
                                        Icon(vectorResource(id = R.drawable.ic_navigate_before_black_18dp))
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(vectorResource(id = R.drawable.ic_clear_black_18dp))
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(vectorResource(id = R.drawable.ic_favorite_border_black_18dp))
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(vectorResource(id = R.drawable.ic_fastfood_black_18dp))
                                    }
                                }
                            }
                        }
                )
            }
        }
    }
}
