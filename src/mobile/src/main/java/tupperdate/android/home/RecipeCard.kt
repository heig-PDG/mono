package tupperdate.android.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTypography
import tupperdate.api.RecipeApi

@Composable
fun RecipeCard(
    offset: Int,
    recipe: RecipeApi.Recipe,
    boxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxSize().offset(x = offset.dp),
        backgroundColor = (Color.Cyan),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(Modifier.fillMaxSize())
        {
            Image(
                // TODO modify parameter to use a String url
                imageResource(id = R.drawable.lobster),
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Row(
                Modifier.fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(5.dp)
            ) {
                Column(Modifier.width(boxWidth * 0.75f)) {
                    Text(recipe.title, style = TupperdateTypography.body1, color = Color.White)
                    Text(
                        recipe.description,
                        style = TupperdateTypography.body2,
                        color = Color.White,
                    )
                }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    BarButton(veryLittleButtonSize,
                        onClick = { // TODO fill this onClick
                        },
                        Color.Transparent, {
                            Icon(vectorResource(id = R.drawable.ic_help_outline_white_18dp))
                        })
                }
            }
        }
    }
}