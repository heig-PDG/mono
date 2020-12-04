package tupperdate.android.home.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.material.BrandedButton

/**
 * A [Dialog] composable that displays two matches.
 *
 * @param myImageUrl the [String] url of the image to display for the user.
 * @param theirImageUrl the [String] url of the image to display for their match.
 * @param onStartChattingClick a callback called when the "start chatting" button is clicked.
 * @param onDismissRequest a callback called when the dialog is dismissed via an external click.
 */
@Composable
fun MatchDialog(
    myImageUrl: String,
    theirImageUrl: String,
    onStartChattingClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest) {
        Card(
            Modifier,
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                Modifier.padding(16.dp),
                Arrangement.spacedBy(24.dp),
                Alignment.CenterHorizontally
            ) {
                // Title.
                Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
                    ProvideTextStyle(MaterialTheme.typography.h6) {
                        Text(stringResource(R.string.matches_title1))
                        Text(stringResource(R.string.matches_title2))
                    }
                }
                // Pics.
                Row(Modifier, Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
                    MatchIcon(myImageUrl, stringResource(R.string.matches_you))
                    Spacer(
                        Modifier
                            .clip(CircleShape)
                            .padding(bottom = 16.dp)
                            .background(Color.Black.copy(alpha = 0.2f))
                            .size(width = 48.dp, height = 2.dp)
                    )
                    MatchIcon(theirImageUrl, stringResource(R.string.matches_them))
                }
                // Button.
                BrandedButton(
                    stringResource(R.string.matches_button_text),
                    onStartChattingClick,
                    Modifier.preferredWidth(218.dp),
                )
            }
        }
    }
}

@Composable
private fun MatchIcon(
    imageUrl: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier, Arrangement.spacedBy(8.dp), Alignment.CenterHorizontally) {
        CoilImage(
            imageUrl,
            Modifier
                .size(56.dp).clip(CircleShape)
                // .shade() is in the spec, but it doesn't look good.
                .border(4.dp, Color.Black.copy(alpha = 0.2f), CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(text, style = MaterialTheme.typography.overline)
    }
}

@Preview
@Composable
private fun MatchDialogPreview() {
    TupperdateTheme {
        MatchDialog(
            myImageUrl = "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            theirImageUrl = "https://www.enviedebienmanger.fr/sites/default/files/demi-langouste_grilleue_au_beurre_et_curry_0.png",
            onStartChattingClick = {},
            onDismissRequest = {},
        )
    }
}
