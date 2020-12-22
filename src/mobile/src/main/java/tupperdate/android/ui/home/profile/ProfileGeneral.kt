package tupperdate.android.ui.home.profile

import androidx.compose.foundation.ScrollableRow
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
import tupperdate.android.data.legacy.api.RecipeApi
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.components.ProfilePicture
import tupperdate.android.ui.theme.material.BrandedButton
import tupperdate.android.ui.theme.modifier.shade
import java.util.List.of

@Composable
fun ProfileGeneral(
    name: String,
    email: String,
    image: Any,
    location: String,
    userRecipes: List<RecipeApi.Recipe>,
    onEditClick: () -> Unit,
    onLocationChange: (String) -> Unit,
    onNewRecipeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO add the new appbar done by G-L for the new navigation
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.profile_title_capital),
            style = MaterialTheme.typography.overline
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
            style = MaterialTheme.typography.overline
        )
        Row(
            modifier = Modifier.fillMaxWidth(1f)
                .padding(top = 16.dp)
        ) {
            BrandedButton(
                value = stringResource(id = R.string.profile_new_recipe), onClick = onNewRecipeClick,
                modifier = Modifier.width((recipeCardWidth * 0.8).dp)
                    .height((recipeCardHeight * 0.8).dp)
                    .padding(end = 16.dp),
                shape = RoundedCornerShape(5.dp)
            )
            LazyRow() {
                items(userRecipes) {
                    DisplayRecipeCard(recipe = it)
                }
            }
        }
    }
}

@Composable
private fun DisplayRecipeCard(
    recipe: RecipeApi.Recipe
) {
    Card(
        modifier = Modifier.height(recipeCardHeight.dp)
            .width(recipeCardWidth.dp)
            .padding(end = 16.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box {
            CoilImage(
                data = recipe.pictureUrl,
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
                    style = MaterialTheme.typography.caption,
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
            Text(text = name, style = MaterialTheme.typography.subtitle1)
            Text(text = email, style = MaterialTheme.typography.subtitle2)
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
        RecipeApi.Recipe(
            "Red lobster",
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            "In the Santa Monica way"
        ),
        RecipeApi.Recipe(
            "Red lobster",
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            "In the Santa Monica way"
        ),
        RecipeApi.Recipe(
            "Red lobster",
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            "In the Santa Monica way"
        ),
        RecipeApi.Recipe(
            "Red lobster",
            "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            "In the Santa Monica way"
        ),
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

private const val recipeCardWidth = 120
private const val recipeCardHeight = 160
