package tupperdate.android.editRecipe

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.material.BrandedButton
import tupperdate.android.R
import tupperdate.api.RecipeApi

@Composable
fun EditRecipePage(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    val (recipeTitle, setRecipeTitle) = remember { mutableStateOf("Lobster") }
    val (recipeDescr, setRecipeDescr) = remember { mutableStateOf("From Santa Monica") }
    Column(modifier.fillMaxSize()) {
        Box(
            modifier = modifier.fillMaxWidth().weight(0.65f)
                .background(Color.Black)
        ) {
            /*Column(modifier.width(constraints.maxWidth.dp).height(pictureBoxHeight.dp),
                horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = {}) {
                    Icon(vectorResource(id = R.drawable.ic_home_dislike_recipe))
                }
                IconButton(onClick = {}) {
                    Icon(vectorResource(id = R.drawable.ic_home_dislike_recipe))
                }
            }*/
        }
        Box(
            modifier = modifier.fillMaxWidth().weight(1f).background(Color.White)
                .padding(5.dp).padding(top = 5.dp)
        ) {
            Column(modifier.fillMaxSize()) {
                OutlinedTextField(
                    modifier = modifier.padding(5.dp).fillMaxWidth(),
                    //TODO find out how to put an empty value
                    value = recipeTitle,
                    onValueChange = { vala -> setRecipeTitle(vala) },
                    label = { Text(stringResource(id = R.string.edit_recipe_label_title)) },
                    placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_title)) },
                )
                Row(
                    modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    BrandedButton(
                        value = "DELETE",
                        onClick = {
                            onDeleteClick()
                        },
                        modifier = modifier.weight(1f, fill = true)
                    )
                    BrandedButton(
                        value = "SAVE",
                        onClick = {
                            RecipeApi.Recipe(recipeTitle, recipeDescr, "")
                            onSaveClick()
                        },
                        modifier = modifier.weight(1f, fill = true),
                    )
                }
                Row(
                    modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val buttonModifier = Modifier.height(75.dp).width(80.dp)
                        .padding(horizontal = 7.dp, vertical = 7.dp)
                    RecipeButton(
                        onClick = {},
                        untoggledText = "NOT VEGGIE",
                        toggledText = "VEGETARIAN",
                        iconToggledId = R.drawable.ic_editrecipe_veggie,
                        iconUntoggledId = R.drawable.ic_editrecipe_not_veggie,
                        modifier = buttonModifier,
                    )
                    RecipeButton(
                        onClick = {},
                        untoggledText = "WARM",
                        toggledText = "COLD",
                        iconToggledId = R.drawable.ic_editrecipe_cold,
                        iconUntoggledId = R.drawable.ic_editrecipe_warm,
                        modifier = buttonModifier,
                    )
                    RecipeButton(
                        onClick = {},
                        untoggledText = "ALLERGENS",
                        toggledText = "ALLERGENS",
                        iconToggledId = R.drawable.ic_editrecipe_allergens,
                        iconUntoggledId = R.drawable.ic_editrecipe_allergens,
                        modifier = buttonModifier,
                    )
                }
                OutlinedTextField(
                    modifier = modifier.padding(top = 10.dp).fillMaxWidth(),
                    value = recipeDescr,
                    label = { Text(stringResource(id = R.string.edit_recipe_label_description)) },
                    placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_description)) },
                    onValueChange = { vala -> setRecipeDescr(vala) },
                )
            }
        }
    }
}

@Composable
fun RecipeButton(
    onClick: () -> Unit,
    untoggledText: String,
    toggledText: String,
    iconToggledId: Int,
    iconUntoggledId: Int,
    modifier: Modifier = Modifier,
) {
    val (iconId, setIconId) = remember { mutableStateOf(iconUntoggledId) }
    val (text, setText) = remember { mutableStateOf(untoggledText) }
    Button(
        modifier = modifier,
        onClick = {
            if (iconId == iconUntoggledId) {
                setIconId(iconToggledId)
                setText(toggledText)
            } else {
                setIconId(iconUntoggledId)
                setText(untoggledText)
            }
        },
        border = null,
        elevation = ButtonConstants.defaultElevation(0.dp, 0.dp, 0.dp),
        colors = ButtonConstants.defaultButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.LightGray
        )
    ) {
        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(asset = vectorResource(id = iconId))
            Text(
                fontSize = 10.sp,
                color = Color.LightGray,
                text = text
            )
        }
    }
}

@Preview
@Composable
private fun EditRecipePageDisconnectPreview() {
    TupperdateTheme {
        EditRecipePage(
            onCloseClick = {},
            onDeleteClick = {},
            onSaveClick = {},
        )
    }
}