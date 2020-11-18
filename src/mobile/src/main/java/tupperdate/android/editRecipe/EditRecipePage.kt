package tupperdate.android.editRecipe

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.material.BrandedButton
import tupperdate.android.R
import java.time.format.TextStyle

@Composable
fun EditRecipePage(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    WithConstraints {
        var pictureBoxHeight = (constraints.maxHeight * 0.45f).toInt()
        val editBoxHeight = constraints.maxHeight - pictureBoxHeight
        pictureBoxHeight += 50

        //Column(Modifier.height(constraints.maxHeight.dp)) {
            Box(
                modifier = modifier.fillMaxWidth().height(pictureBoxHeight.dp)
                    .background(Color.Black)
            )
            Box(
                modifier = modifier.fillMaxWidth().height(editBoxHeight.dp).background(Color.White)
                    .padding(5.dp).padding(top = 5.dp).offset(y=(pictureBoxHeight-50).dp)
            )
            {
                Column(modifier.fillMaxSize()) {
                    OutlinedTextField(
                        modifier = modifier.padding(5.dp).fillMaxWidth(),
                        //TODO find out how to put an empty value
                        value = "Title", onValueChange = {},
                        label = { Text(stringResource(id = R.string.edit_recipe_label_title)) },
                        placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_title)) },
                    )
                    Row(modifier.fillMaxWidth().padding(5.dp))
                    {
                        BrandedButton(
                            value = "DELETE",
                            onClick = {},
                            modifier = modifier.fillMaxWidth(0.5f).padding(end = 5.dp)
                        )
                        BrandedButton(
                            value = "SAVE",
                            onClick = {},
                            modifier = modifier.fillMaxWidth(),
                        )
                    }
                    Row(
                        modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val buttonModifier = Modifier.height(75.dp).width(80.dp)
                            .padding(horizontal = 7.dp, vertical = 7.dp)
                        RecipeButton(
                            buttonModifier,
                            onClick = {},
                            style = MaterialTheme.typography.caption,
                            text = "VEGETARIAN",
                            iconId = R.drawable.ic_editrecipe_veggie
                        )
                        RecipeButton(
                            buttonModifier,
                            onClick = {},
                            style = MaterialTheme.typography.caption,
                            text = "COLD",
                            iconId = R.drawable.ic_editrecipe_cold
                        )
                        RecipeButton(
                            buttonModifier,
                            onClick = {},
                            style = MaterialTheme.typography.caption,
                            text = "ALLERGENS",
                            iconId = R.drawable.ic_editrecipe_allergens
                        )
                    }
                    OutlinedTextField(
                        modifier = modifier.padding(top = 10.dp).fillMaxWidth(),
                        value = "This is...",
                        label = { Text(stringResource(id = R.string.edit_recipe_label_description)) },
                        placeholder = { Text(stringResource(id = R.string.edit_recipe_placeholder_description)) },
                        onValueChange = {},
                    )
                }
            }
        }
    //}
}

@Composable
fun RecipeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    style: androidx.compose.ui.text.TextStyle,
    text: String,
    iconId: Int
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        border = null,
        elevation = ButtonConstants.defaultElevation(0.dp, 0.dp, 0.dp),
        colors = ButtonConstants.defaultButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.LightGray
        )
    ) {
        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(asset = vectorResource(id = iconId))
            Text(
                //style = style,
                fontSize = TextUnit(2),
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