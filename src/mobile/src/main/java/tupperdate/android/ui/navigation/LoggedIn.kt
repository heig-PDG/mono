package tupperdate.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.*
import tupperdate.android.data.legacy.api.Api
import tupperdate.android.data.legacy.api.RecipeApi
import tupperdate.android.ui.home.Home
import tupperdate.android.ui.home.HomeSections
import tupperdate.android.ui.home.recipe.NewRecipe
import tupperdate.android.ui.home.recipe.ViewRecipe

private object LoggedInDestination {
    const val NEW_RECIPE = "newRecipe"
    const val VIEW_RECIPE = "viewRecipe/{recipe}"
    const val FEED = "feed"
}

/**
 * The composable that manages the app navigation when the user is currently logged in.
 *
 * @param api the [Api] to manage data.
 */
@Composable
fun LoggedIn(
    api: Api,
) {
    LaunchedEffect(true) { api.users.updateProfile() }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LoggedInDestination.FEED) {
        composable(LoggedInDestination.NEW_RECIPE) {
            NewRecipe(
                recipeApi = api.recipe,
                imagePickerApi = api.images,
                onBack = { navController.navigateUp() },
            )
        }
        composable(
            LoggedInDestination.VIEW_RECIPE,
            arguments = listOf(navArgument("recipe") {
                type = NavType.ParcelableType(RecipeApi.Recipe::class.java)
            }),
            // TODO: pass recipe's identifier instead of parcelize
        ) {
            it.arguments?.getParcelable<RecipeApi.Recipe?>("recipe")?.let { recipe ->
                ViewRecipe(
                    recipeApi = api.recipe,
                    recipe = recipe,
                    onBack = { /*TODO*/ },
                )
            }
        }
        composable(LoggedInDestination.FEED) {
            Home(
                api = api,
                onNewRecipeClick = { navController.navigate(LoggedInDestination.NEW_RECIPE) },
                onBack = { navController.navigateUp() },
                onDevClick = { /*TODO*/ },
                startingSection = HomeSections.Feed,
            )
        }
    }

    /*
when (destination) {
    is LoggedInDestination.NewRecipe -> NewRecipe(
        recipeApi = api.recipe,
        imagePickerApi = api.images,
        onBack = action.back,
    )
    is LoggedInDestination.ViewRecipe -> ViewRecipe(
        recipeApi = api.recipe,
        recipe = destination.recipe,
        onBack = action.back,
    )
    is LoggedInDestination.Home -> Home(
        api = api,
        onBack = action.back,
        onDevClick = action.authenticationTesting,
    )
    is LoggedInDestination.AuthenticationTesting ->
        AuthenticationTesting(api)
}

     */
}
