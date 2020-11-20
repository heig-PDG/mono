package tupperdate.android.editRecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme

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
                //this button is invisible but really exists
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
        MainSurface(
            onDeleteClick = onDeleteClick, onSaveClick = onSaveClick,
            modifier = modifier.weight(1f)
        )
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
