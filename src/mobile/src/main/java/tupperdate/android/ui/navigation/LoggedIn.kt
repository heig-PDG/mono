package tupperdate.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType.StringType
import androidx.navigation.compose.*
import tupperdate.android.ui.home.Home
import tupperdate.android.ui.home.HomeSections
import tupperdate.android.ui.home.recipe.NewRecipe
import tupperdate.android.ui.home.recipe.ViewRecipe

/**
 * Available destinations when the user is logged in
 */
private object LoggedInDestination {
    const val NEW_RECIPE = "newRecipe"
    const val VIEW_RECIPE = "viewRecipe/{recipe}"
    const val FEED = "feed"
    const val PROFILE = "profile"
    const val CONVERSATIONS = "conversations"

    fun viewRecipe(identifier: String): String {
        return "viewRecipe/$identifier"
    }
}

/**
 * The composable that manages the app navigation when the user is currently logged in.
 */
@Composable
fun LoggedIn() {
    LaunchedEffect(true) {
        // api.users.updateProfile()
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LoggedInDestination.FEED) {
        composable(LoggedInDestination.NEW_RECIPE) {
            NewRecipe(
                onBack = navController::navigateUp,
            )
        }
        composable(
            LoggedInDestination.VIEW_RECIPE,
            arguments = listOf(navArgument("recipe") { type = StringType }),
        ) {
            it.arguments?.getString("recipe")?.let { recipe ->
                ViewRecipe(
                    identifier = recipe,
                    onBack = { navController.navigateUp() },
                )
            }
        }
        composable(LoggedInDestination.CONVERSATIONS) {
            Home(
                onNewRecipeClick = { navController.navigate(LoggedInDestination.NEW_RECIPE) },
                onRecipeDetailsClick = {
                    navController.navigate(LoggedInDestination.viewRecipe(it.identifier))
                },
                onBack = { navController.navigateUp() },
                onDevClick = { },
                startingSection = HomeSections.Conversations,
            )
        }
        composable(LoggedInDestination.FEED) {
            Home(
                onNewRecipeClick = { navController.navigate(LoggedInDestination.NEW_RECIPE) },
                onRecipeDetailsClick = {
                    navController.navigate(LoggedInDestination.viewRecipe(it.identifier))
                },
                onBack = { navController.navigateUp() },
                onDevClick = { },
                startingSection = HomeSections.Feed,
            )
        }
        composable(LoggedInDestination.PROFILE) {
            Home(
                onNewRecipeClick = { navController.navigate(LoggedInDestination.NEW_RECIPE) },
                onRecipeDetailsClick = {
                    navController.navigate(LoggedInDestination.viewRecipe(it.identifier))
                },
                onBack = { navController.navigateUp() },
                onDevClick = { },
                startingSection = HomeSections.Profile,
            )
        }
    }
}