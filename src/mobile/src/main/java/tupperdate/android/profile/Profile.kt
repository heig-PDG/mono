package tupperdate.android.profile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.components.ProfilePicture
import tupperdate.android.ui.material.BrandedButton
import tupperdate.api.ImagePickerApi
import tupperdate.api.ImageType
import tupperdate.api.UserApi

@Composable
fun Profile(
    userApi: UserApi,
    imagePicker: ImagePickerApi,
    profile: UserApi.Profile,
    onCloseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val initName = profile.displayName ?: ""
    val (name, setName) = remember(profile) { mutableStateOf(initName) }

    val newProfilePic by remember { imagePicker.currentProfile }.collectAsState(initial = null)

    val profileImage = profile.profileImageUrl ?: "https://via.placeholder.com/150"

    val profilePic = newProfilePic
        ?: Uri.parse(profileImage)

    Profile(
        name = name,
        imageUrl = profilePic,
        onNameChange = setName,
        onCloseClick = onCloseClick,
        onEditPictureClick = { imagePicker.pick(ImageType.Profile) },
        onSaveClick = {
            scope.launch { userApi.putProfile(name, newProfilePic) }
            onCloseClick()
        },
        onSignOutClick = onSignOutClick,
        modifier = modifier,
    )
}

@Composable
private fun Profile(
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
                style = MaterialTheme.typography.h6
            )
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .size(32.dp)
            ) {
                Icon(
                    asset = vectorResource(R.drawable.ic_home_dislike_recipe),
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Box(modifier = Modifier
            .padding(top = 40.dp, bottom = 37.dp)
            .align(Alignment.CenterHorizontally)
        ) {
            ProfilePicture(
                image = imageUrl,
                highlighted = false,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = onEditPictureClick,
                    )
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
            colors = ButtonConstants.defaultButtonColors(
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
private fun ProfilePreview() {
    val (name, setName) = remember { mutableStateOf("Thor") }
    TupperdateTheme {
        Profile(
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
