package tupperdate.android.ui.home.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.ProfileEmail
import tupperdate.android.ui.theme.ProfileName
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.TupperdateTypography
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.material.BrandedButton
import tupperdate.android.ui.theme.modifier.shade

@Composable
fun ProfileGeneral(
    name: String,
    email: String,
    image: Any,
    location: String,
    userRecipes: List<Recipe>,
    onEditClick: () -> Unit,
    onLocationChange: (String) -> Unit,
    onNewRecipeClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.profile_title_capital),
            style = TupperdateTypography.overline
        )
        ProfileRecap(name = name, email = email, image = image, onEditClick = onEditClick)
        OutlinedTextField(
            value = location,
            onValueChange = onLocationChange,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 4.dp, bottom = 24.dp),
            label = { Text(stringResource(R.string.profile_location)) },
            placeholder = { location }
        )
        Text(
            text = stringResource(id = R.string.profile_tupps),
            style = TupperdateTypography.overline
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(1f)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                BrandedButton(
                    value = stringResource(id = R.string.profile_new_recipe),
                    onClick = onNewRecipeClick,
                    modifier = Modifier.width(RecipeCardWidth)
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(RecipeCardHeight)
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
                modifier = Modifier.align(Alignment.BottomStart)
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
            Text(text = name, style = TupperdateTypography.subtitle1, color = Color.ProfileName)
            Text(text = email, style = TupperdateTypography.subtitle2, color = Color.ProfileEmail)
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(
            onClick = onEditClick,
            modifier = Modifier.height(19.dp)
        ) {
            Icon(
                imageVector = vectorResource(id = R.drawable.ic_editrecipe_edit),
            )
        }
    }
}

@Preview
@Composable
fun ProfileGeneralPreview() {
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
        ProfileGeneral(name = "Aloy", email = "chieftain@banuk",
            image = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            location = "Song's Edge",
            userRecipes = reList,
            onEditClick = {},
            onLocationChange = {},
            onNewRecipeClick = {})
    }
}

private val RecipeCardWidth = 120.dp
private val RecipeCardHeight = 160.dp
