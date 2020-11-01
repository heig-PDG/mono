package tupperdate.android.home

import tupperdate.api.RecipeApi

// This method exists because the provider of the next recipe will be more complex,
// including datas of the user (veggie, for example)
fun getNewRecipe(): RecipeApi.Recipe{
    return RecipeApi.Recipe("Red lobster",
        "Look at me, am I not yummy ?",
        "lobster.jpg")
}