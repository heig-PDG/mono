package tupperdate.android.editRecipe

import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
            Column(
                Modifier.matchParentSize().padding(8.dp),
                horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onCloseClick() }) {
                    Icon(vectorResource(id = R.drawable.ic_home_dislike_recipe))
                }
                IconButton(
                    onClick = {},
                    Modifier.background(color = Color.Transparent)
                ) {
                    Icon(
                        vectorResource(id = R.drawable.ic_editrecipe_edit),
                        tint = Color.White //why is the param named "tint" and not "color" ?! seriously, Google...
                    )
                }
            }
        }
        Surface(modifier = Modifier.fillMaxSize().weight(1f),
            shape = RoundedCornerShape(topLeft = 8.dp, topRight = 8.dp)) {
            Box(
                modifier = modifier.background(Color.White)
                    .padding(8.dp).padding(top = 8.dp),
            ) {
                Column(modifier.fillMaxSize()) {
                    OutlinedTextField(
                        modifier = modifier.padding(8.dp).fillMaxWidth(),
                        value = recipeTitle,
                        onValueChange = { vala -> setRecipeTitle(vala) },
                        label = { Text(stringResource(id = R.string.edit_recipe_label_title)) },
                        placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_title)) },
                    )
                    Row(
                        modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BrandedButton(
                            value = "DELETE",
                            onClick = {
                                onDeleteClick()
                            },
                            modifier = modifier.weight(1f, fill = true),
                            shape = RoundedCornerShape(8.dp)
                        )
                        BrandedButton(
                            value = "SAVE",
                            onClick = {
                                RecipeApi.Recipe(recipeTitle, recipeDescr, "")
                                onSaveClick()
                            },
                            modifier = modifier.weight(1f, fill = true),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                    Row(
                        modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                    ) {
                        val buttonModifier = Modifier
                            //this is the only way to have one-line buttons
                            .width(80.dp).height(75.dp)
                        RecipeButton(
                            untoggledText = "NOT VEGGIE",
                            toggledText = "VEGETARIAN",
                            iconToggledId = R.drawable.ic_editrecipe_not_veggie,
                            iconUntoggledId = R.drawable.ic_editrecipe_veggie,
                            buttonModifier
                        )
                        Divider(color = Color.Gray, thickness = 2.dp)
                        RecipeButton(
                            untoggledText = "WARM",
                            toggledText = "COLD",
                            iconToggledId = R.drawable.ic_editrecipe_cold,
                            iconUntoggledId = R.drawable.ic_editrecipe_warm,
                            buttonModifier
                        )
                        Divider(color = Color.Gray, thickness = 2.dp)
                        RecipeButton(
                            untoggledText = "CLEAN",
                            toggledText = "ALLERGENS",
                            iconToggledId = R.drawable.ic_editrecipe_allergens, //TODO find a crossed allergen icon
                            iconUntoggledId = R.drawable.ic_editrecipe_allergens,
                            buttonModifier
                        )
                    }
                    OutlinedTextField(
                        modifier = modifier.padding(top = 8.dp).fillMaxWidth(),
                        value = recipeDescr,
                        label = { Text(stringResource(id = R.string.edit_recipe_label_description)) },
                        placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_description)) },
                        onValueChange = { vala -> setRecipeDescr(vala) },
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeButton(
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
            contentColor = Color.Gray
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