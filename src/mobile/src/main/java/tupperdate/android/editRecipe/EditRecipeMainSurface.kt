package tupperdate.android.editRecipe

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
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
import tupperdate.android.R
import tupperdate.android.ui.material.BrandedButton
import tupperdate.api.RecipeApi

@Composable
fun MainSurface(
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier=Modifier
) {
    val (recipeTitle, setRecipeTitle) = remember { mutableStateOf("Lobster") }
    val (recipeDescr, setRecipeDescr) = remember { mutableStateOf("From Santa Monica") }

    //TODO find why input fields appear too big and fix it
    Surface(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(topLeft = 8.dp, topRight = 8.dp)
    ) {
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
                        //this is the only way to have one-line buttons with a 80 dp size
                        .width(80.dp).height(75.dp)
                    RecipeButton(
                        untoggledText = "NOT VEGGIE",
                        toggledText = "VEGETARIAN",
                        iconToggledId = R.drawable.ic_editrecipe_not_veggie,
                        iconUntoggledId = R.drawable.ic_editrecipe_veggie,
                        buttonModifier
                    )
                    RecipeButton(
                        untoggledText = "WARM",
                        toggledText = "COLD",
                        iconToggledId = R.drawable.ic_editrecipe_cold,
                        iconUntoggledId = R.drawable.ic_editrecipe_warm,
                        buttonModifier
                    )
                    RecipeButton(
                        untoggledText = "CLEAN",
                        toggledText = "ALLERGENS",
                        iconToggledId = R.drawable.ic_editrecipe_allergens, //TODO find a crossed allergen icon
                        iconUntoggledId = R.drawable.ic_editrecipe_allergens,
                        buttonModifier
                    )
                }
                Divider(color = Color.Gray, thickness = 1.dp)
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