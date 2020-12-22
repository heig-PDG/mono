package tupperdate.android.ui.home.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.grey3
import tupperdate.android.ui.theme.grey6

@Composable
fun ProfileGeneral(
    name: String,
    email: String,
    image: Any,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO add an appbar
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "YOUR PROFILE", style = MaterialTheme.typography.overline)
        ProfileRecap(name = name, email = email, image = image, onEditClick = onEditClick)
    }
}

@Composable
private fun ProfileRecap(
    name: String,
    email: String,
    image: Any,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(end = 3.dp, top = 16.dp, bottom = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            image = image,
            highlighted = false,
            modifier = Modifier.size(56.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = name, style = MaterialTheme.typography.subtitle1, color = Color.grey6)
            Text(text = email, style = MaterialTheme.typography.subtitle2, color = Color.grey3)
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(
            onClick = onEditClick,
            modifier = Modifier.height(19.dp)
        ) {
            Icon(
                imageVector = vectorResource(id = R.drawable.ic_editrecipe_edit),
                tint = Color.grey6
            )
        }
    }
}

@Preview
@Composable
fun ProfileGeneralPreview() {
    TupperdateTheme {
        ProfileGeneral(name = "Aloy", email = "chieftain@banuk",
            image = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            onEditClick = {})
    }
}
