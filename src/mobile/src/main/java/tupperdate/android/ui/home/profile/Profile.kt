package tupperdate.android.ui.home.profile

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.legacy.api.ImagePickerApi
import tupperdate.android.data.legacy.api.ImageType
import tupperdate.android.data.legacy.api.UserApi
import tupperdate.android.ui.theme.ProfileEmail
import tupperdate.android.ui.theme.ProfileName
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.material.BrandedButton
import tupperdate.android.ui.theme.modifier.shade

@Composable
fun Profile(
    userApi: UserApi,
    imagePicker: ImagePickerApi,
    profile: UserApi.Profile,
    onCloseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDevClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = AmbientLifecycleOwner.current.lifecycleScope

    val initName = profile.displayName ?: ""
    val (name, setName) = remember(profile) { mutableStateOf(initName) }

    val phone = profile.phone

    val newProfilePic by remember { imagePicker.currentProfile }.collectAsState(initial = null)
    val profileImage = profile.profileImageUrl ?: "https://via.placeholder.com/150"
    val profilePic = newProfilePic ?: Uri.parse(profileImage)

    val (editing, setEditing) = remember { mutableStateOf(false) }

    Profile(
        name = name,
        phone = phone,
        profilePicture = profilePic,
        location = "",
        userRecipes = listOf(),
        editing = editing,
        onEditClick = {
            setEditing(true)
        },
        onNameChange = setName,
        onSaveClick = {
            setEditing(false)
        },
        onPictureClick = {
            imagePicker.pick(ImageType.Profile)
        },
        onLocationChange = {},
        onNewRecipeClick = {},
        modifier = modifier,
    )
}


@Composable
private fun Profile(
    name: String,
    phone: String,
    profilePicture: Any,
    location: String,
    userRecipes: List<Recipe>,
    editing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onPictureClick: () -> Unit,
    onLocationChange: (String) -> Unit,
    onNewRecipeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(R.string.profile_title_capital),
            style = TupperdateTypography.overline
        )
        ProfileRecap(
            name = name,
            phone = phone,
            image = profilePicture,
            editing = editing,
            onEditClick = onEditClick,
            onNameChange = onNameChange,
            onSaveClick = onSaveClick,
            onPictureClick = onPictureClick,
        )
        OutlinedTextField(
            value = location,
            onValueChange = onLocationChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 24.dp),
            label = { Text(stringResource(R.string.profile_location)) },
            placeholder = { Text(stringResource(R.string.profile_location_placeholder)) },
        )
        Text(
            text = stringResource(R.string.profile_tupps),
            style = TupperdateTypography.overline
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                BrandedButton(
                    value = stringResource(R.string.profile_new_recipe),
                    onClick = onNewRecipeClick,
                    modifier = Modifier
                        .width(RecipeCardWidth)
                        .height(RecipeCardHeight)
                        .padding(end = 16.dp),
                    shape = RoundedCornerShape(5.dp)
                )
            }
            items(userRecipes) {
                DisplayRecipeCard(
                    recipe = it,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun DisplayRecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(RecipeCardHeight)
            .width(RecipeCardWidth),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box {
            CoilImage(
                data = recipe.picture,
                modifier = Modifier.shade().fillMaxSize(),
                fadeIn = true,
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = TupperdateTypography.caption,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ProfileRecap(
    name: String,
    phone: String,
    image: Any,
    editing: Boolean,
    onEditClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onPictureClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 3.dp, top = 16.dp, bottom = 16.dp)
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            image = image,
            highlighted = false,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .clickable(onClick = onPictureClick),
        )

        if (editing) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                trailingIcon = {
                    IconButton(onClick = onSaveClick) {
                        Icon(imageVector = vectorResource(R.drawable.ic_content_save))
                    }
                },
                modifier = Modifier.padding(start = 16.dp).weight(1f),
            )
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = name,
                    style = TupperdateTypography.subtitle1,
                    color = Color.ProfileName,
                )
                Text(
                    text = phone,
                    style = TupperdateTypography.subtitle2,
                    color = Color.ProfileEmail,
                )
            }

            Spacer(modifier = Modifier.weight(1f, true))

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.height(18.dp)
            ) {
                Icon(imageVector = vectorResource(R.drawable.ic_editrecipe_edit))
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    val reList = listOf(
        Recipe(
            "Red lobster",
            "Red lobster",
            "In the Santa Monica way",
            2077,
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg"
        ),
        Recipe(
            "Red lobster",
            "Red lobster",
            "In the Santa Monica way",
            2077,
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg"
        ),
        Recipe(
            "Red lobster",
            "Red lobster",
            "In the Santa Monica way",
            2077,
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg"
        ),
        Recipe(
            "Red lobster",
            "Red lobster",
            "In the Santa Monica way",
            2077,
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg"
        )
    )
    TupperdateTheme {
        val (name, setName) = remember { mutableStateOf("Aloy") }
        val (editing, setEditing) = remember { mutableStateOf(false) }

        Profile(
            name = name,
            phone = "079 123 55 41",
            profilePicture = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            location = "Song's Edge",
            userRecipes = reList,
            editing = editing,
            onEditClick = { setEditing(true) },
            onNameChange = setName,
            onSaveClick = { setEditing(false) },
            onPictureClick = {},
            onLocationChange = {},
            onNewRecipeClick = {})
    }
}

private val RecipeCardWidth = 120.dp
private val RecipeCardHeight = 160.dp
