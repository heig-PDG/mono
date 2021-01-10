package tupperdate.android.ui.onboarding

import android.net.Uri
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.material.BrandedButton

@Composable
fun OnboardingProfile(
    name: String,
    image: Any,
    onNameChange: (String) -> Unit,
    onEditPictureClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold {
        ScrollableColumn(modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.h6,
            )
            Box(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 37.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                ProfilePicture(
                    image = image,
                    highlighted = false,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onEditPictureClick),
                )
                Text(
                    text = stringResource(R.string.profile_editpic),
                    style = MaterialTheme.typography.overline,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp), // Depart from Figma prototype. 13dp is too much
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 37.dp),
                label = { Text(stringResource(R.string.profile_name)) },
                placeholder = { Text(stringResource(R.string.profile_name_placeholder)) }
            )

            BrandedButton(
                value = stringResource(R.string.profile_save),
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingProfilePreview() = TupperdateTheme {
    val (name, setName) = remember { mutableStateOf("Thor") }
    OnboardingProfile(
        name = name,
        image = Uri.parse("https://www.thispersondoesnotexist.com/image"),
        onNameChange = setName,
        onEditPictureClick = {},
        onSaveClick = {},
    )
}
