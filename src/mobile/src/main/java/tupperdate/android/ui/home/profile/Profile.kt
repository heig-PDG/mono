package tupperdate.android.ui.home.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tupperdate.android.R
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.features.recipe.RecipeAttributes
import tupperdate.android.ui.ambients.AmbientImagePicker
import tupperdate.android.ui.ambients.AmbientProfile
import tupperdate.android.ui.theme.*
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.material.BrandedButton

@Composable
fun Profile(
    onNewRecipeClick: () -> Unit,
    onOwnRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Add location to profiles
    val picker = AmbientImagePicker.current
    val viewModel = getViewModel<ProfileViewModel> { parametersOf(picker) }
    val editing by viewModel.editing.collectAsState(false)
    val recipes by viewModel.recipes.collectAsState(emptyList())

    val profile = AmbientProfile.current
    val profileName = (profile as? AuthenticationStatus.Displayable)?.displayName ?: ""
    val profileImage = (profile as? AuthenticationStatus.Displayable)?.displayPictureUrl
        ?: PlaceholderProfileImage
    val phone = (profile as? AuthenticationStatus.Displayable)?.phoneNumber ?: ""
    val (name, setName) = remember(profile) { mutableStateOf(profileName) }

    Profile(
        profileName = name,
        profilePhone = phone,
        profilePicture = profileImage,
        profileEditing = editing,
        onProfileNameChanged = setName,
        onProfileEditClick = viewModel::onEditClick,
        onProfileSaveClick = {
            // TODO : Let the ViewModel own the text state.
            viewModel.onSave(name)
        },
        onProfilePictureClick = viewModel::onProfilePictureClick,
        recipes = recipes,
        onRecipeNewClick = onNewRecipeClick,
        onRecipeOwnClick = onOwnRecipeClick,
        modifier = modifier,
    )
}

@Composable
private fun Profile(
    // Profile management.
    profileName: String,
    profilePhone: String,
    profilePicture: Any,
    profileEditing: Boolean,
    onProfileNameChanged: (String) -> Unit,
    onProfileEditClick: () -> Unit,
    onProfileSaveClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
    // Recipe management.
    recipes: List<Recipe>,
    onRecipeNewClick: () -> Unit,
    onRecipeOwnClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.profile_title_capital),
                style = TupperdateTypography.overline,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            ProfileRecap(
                name = profileName,
                phone = profilePhone,
                image = profilePicture,
                editing = profileEditing,
                onEditClick = onProfileEditClick,
                onNameChange = onProfileNameChanged,
                onSaveClick = onProfileSaveClick,
                onPictureClick = onProfilePictureClick,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            Text(
                text = stringResource(R.string.profile_tupps),
                style = TupperdateTypography.overline,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            ) {
                item {
                    BrandedButton(
                        value = stringResource(R.string.profile_new_recipe),
                        onClick = onRecipeNewClick,
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp),
                        shape = RoundedCornerShape(5.dp)
                    )
                }
                items(recipes) {
                    ProfileRecipe(it, onClick = { onRecipeOwnClick(it) })
                }
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
        modifier = modifier.fillMaxWidth().height(72.dp),
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
            identifier = "Red lobster",
            owner = "Lobster owner",
            title = "Red lobster",
            description = "In the Santa Monica way",
            timestamp = 2077,
            picture = "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            attributes = RecipeAttributes(
                vegetarian = false,
                warm = true,
                hasAllergens = false,
            ),
        ),
    )
    TupperdateTheme {
        val (name, setName) = remember { mutableStateOf("Aloy") }
        val (editing, setEditing) = remember { mutableStateOf(false) }

        Profile(
            profileName = name,
            profilePhone = "079 123 55 41",
            profilePicture = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            profileEditing = editing,
            onProfileNameChanged = setName,
            onProfileEditClick = { setEditing(true) },
            onProfileSaveClick = { setEditing(false) },
            onProfilePictureClick = { /*TODO*/ },
            recipes = reList,
            onRecipeNewClick = { /*TODO*/ },
            onRecipeOwnClick = { /*TODO*/ })
    }
}
