package tupperdate.android.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
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
import tupperdate.api.UserApi

@Composable
fun Profile(
    userApi: UserApi,
    profile: UserApi.Profile,
    onCloseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val initName = profile.displayName ?: ""
    val (name, setName) = remember { mutableStateOf(initName) }

    val profilePic = profile.profileImageUrl ?: "https://via.placeholder.com/150"

    Profile(
        name = name,
        imageUrl = profilePic,
        onNameChange = setName,
        onCloseClick = onCloseClick,
        onEditClick = {},
        onSaveClick = {
            scope.launch { userApi.putProfile(name) }
            onCloseClick()
        },
        onSignOutClick = onSignOutClick,
        modifier = modifier,
    )
}

@Composable
private fun Profile(
    name: String,
    imageUrl: String,
    onNameChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit,
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

        Row(
            Modifier.padding(top = 40.dp, bottom = 37.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column(
                horizontalAlignment = (Alignment.CenterHorizontally),
                verticalArrangement = (Arrangement.spacedBy((-36).dp))
            ) {
                ProfilePicture(
                    image = imageUrl,
                    highlighted = false,
                    modifier = Modifier.size(96.dp)
                )
                Button(
                    onClick = onEditClick,
                    colors = ButtonConstants.defaultButtonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonConstants.defaultElevation(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.profile_editpic),
                        style = MaterialTheme.typography.overline
                    )
                }
            }
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
            imageUrl = "https://www.thispersondoesnotexist.com/image",
            onNameChange = setName,
            onCloseClick = {},
            onEditClick = {},
            onSaveClick = {},
            onSignOutClick = {},
        )
    }
}
