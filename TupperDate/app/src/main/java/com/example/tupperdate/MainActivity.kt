package com.example.tupperdate

import android.graphics.Paint
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import com.example.tupperdate.ui.TupperDateTheme
import java.lang.reflect.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TupperDateTheme {
                TopAppBar(
                    title = {
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text (text = "tupper.date", textAlign = TextAlign.Center)
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
        }
    }
}