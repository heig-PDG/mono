package tupperdate.android.ui.home.profile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf
import tupperdate.android.R
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.ui.ambients.AmbientPhoneRegistration
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.material.BrandedButton

@Composable
private fun ProfileSheet(
    name: String,
    imageUrl: Uri,
    onNameChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onEditPictureClick: () -> Unit,
    onSaveClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScrollableColumn(modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.h6,
            )
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = vectorResource(R.drawable.ic_home_dislike_recipe),
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 37.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            ProfilePicture(
                image = imageUrl,
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

        Button(
            onClick = onSignOutClick,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
                .preferredHeight(56.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Red,
                backgroundColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.profile_sign_out),
                color = Color.Red
            )
        }
    }
}

@Preview
@Composable
private fun ProfileSheetPreview() {
    val (name, setName) = remember { mutableStateOf("Thor") }
    TupperdateTheme {
        ProfileSheet(
            name = name,
            imageUrl = Uri.parse("https://www.thispersondoesnotexist.com/image"),
            onNameChange = setName,
            onCloseClick = {},
            onEditPictureClick = {},
            onSaveClick = {},
            onSignOutClick = {},
        )
    }
}
