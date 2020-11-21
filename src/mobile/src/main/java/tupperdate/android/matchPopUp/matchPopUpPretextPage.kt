package tupperdate.android.matchPopUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.RecipeApi

/*
TODO delete this file once merged
 */

@Composable
fun MatchPopUpPretextPage(
    onChattingClick:()->Unit,
    recipe1: RecipeApi.Recipe,
    recipe2: RecipeApi.Recipe,
){
    Column(Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
            MatchPopUp(onChattingClick = onChattingClick, recipe1 = recipe1, recipe2 = recipe2)
        }
    }
}

@Preview
@Composable
fun MatchPopUpPreview(){
    TupperdateTheme {
        MatchPopUpPretextPage(
            onChattingClick = {},
            recipe1 = RecipeApi.Recipe("Lobster", "Red lobster from Santa Monica", "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg"),
            recipe2 = RecipeApi.Recipe("Langusta", "Langusta from Britain", "https://www.enviedebienmanger.fr/sites/default/files/demi-langouste_grilleue_au_beurre_et_curry_0.png")
        )
    }
}