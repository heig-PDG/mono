package tupperdate.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType.StringType
import androidx.navigation.compose.*
import org.koin.androidx.compose.getViewModel
import tupperdate.android.ui.home.Home
import tupperdate.android.ui.home.chats.Chat
import tupperdate.android.ui.home.feed.MatchDialog
import tupperdate.android.ui.home.recipe.NewRecipe
import tupperdate.android.ui.home.recipe.ViewRecipe

/**
 * Available destinations when the user is logged in
 */
private object LoggedInDestination {
    const val HOME = "home"
    const val NEW_RECIPE = "newRecipe"
    const val VIEW_RECIPE = "viewRecipe/{recipe}"
    const val VIEW_CONVERSATION = "conversation/{id}"

    fun viewConversation(identifier: String): String {
        return "conversation/$identifier"
    }

    fun viewRecipe(identifier: String): String {
        return "viewRecipe/$identifier"
    }
}

/**
 * The composable that manages the app navigation when the user is currently logged in.
 */
@Composable
fun LoggedIn() {

    val viewModel = getViewModel<LoggedInViewModel>()
    val match = viewModel.match.collectAsState(null).value

    if (match != null) {
        MatchDialog(
            myImageUrl = match.myPicture,
            theirImageUrl = match.theirPicture,
            onStartChattingClick = { viewModel.onAccept(match) },
            onDismissRequest = { viewModel.onAccept(match) },
        )
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LoggedInDestination.HOME) {
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
        composable(
            LoggedInDestination.VIEW_CONVERSATION,
            listOf(navArgument("id") { type = StringType })
        ) {
            it.arguments?.getString("id")?.let { id ->
                Chat(
                    id = id,
                    onBack = navController::navigateUp,
                )
            }
        }
        composable(LoggedInDestination.HOME) {
            Home(
                onNewRecipeClick = { navController.navigate(LoggedInDestination.NEW_RECIPE) },
                onRecipeDetailsClick = {
                    navController.navigate(LoggedInDestination.viewRecipe(it.identifier))
                },
                onConversationClick = {
                    navController.navigate(LoggedInDestination.viewConversation(it))
                },
            )
        }
    }
}
