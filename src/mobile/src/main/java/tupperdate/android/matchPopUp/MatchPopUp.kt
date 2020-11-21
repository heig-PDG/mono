package tupperdate.android.matchPopUp

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.ui.material.BrandedButton
import tupperdate.api.RecipeApi
import tupperdate.android.R

@Composable
fun MatchPopUp(
    onChattingClick: () -> Unit,
    recipe1: RecipeApi.Recipe,
    recipe2: RecipeApi.Recipe,
    modifier: Modifier = Modifier
) {
    Card(
        modifier.height(248.dp).width(250.dp)
            .padding(top = 12.dp, end = 16.dp, bottom = 16.dp, start = 16.dp)
            .background(Color.White),
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Row(Modifier.height(44.dp).fillMaxWidth()) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.matches_title1),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = stringResource(id = R.string.matches_title2),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
            Spacer(modifier = Modifier.preferredHeight(24.dp))
            Row(
                Modifier.height(72.dp).align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImagesMatching(
                    urlImage1 = recipe1.pictureUrl,
                    text = stringResource(id = R.string.matches_you)
                )
                Divider(Modifier.width(48.dp).padding(start = 4.dp, end = 4.dp))
                ImagesMatching(
                    urlImage1 = recipe2.pictureUrl,
                    text = stringResource(id = R.string.matches_them)
                )
            }
            Spacer(modifier = Modifier.preferredHeight(28.dp))
            Row(Modifier.height(44.dp).align(Alignment.CenterHorizontally)) {
                BrandedButton(value = stringResource(id = R.string.matches_button_text),
                    onClick = { onChattingClick() })
            }
        }
    }
}

@Composable
fun ImagesMatching(
    urlImage1: String,
    text: String
) {
    Column(
        Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilImage(
            data = urlImage1,
            modifier = Modifier.size(56.dp).clip(CircleShape)
                .border(2.dp, Color.Gray, shape = CircleShape),
            contentScale = ContentScale.Crop
        ) {
        }
        Text(text = text, fontSize = 10.sp)
    }
}