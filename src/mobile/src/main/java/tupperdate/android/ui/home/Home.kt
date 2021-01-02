package tupperdate.android.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.data.legacy.api.Api
import tupperdate.android.ui.home.chats.Conversations
import tupperdate.android.ui.home.feed.Feed
import tupperdate.android.ui.home.profile.Profile
import tupperdate.android.ui.theme.Flamingo500
import tupperdate.android.ui.theme.InactiveIcons
import tupperdate.android.ui.theme.Smurf500
import tupperdate.android.ui.theme.TupperdateTypography

@Composable
fun Home(
    api: Api,
    onNewRecipeClick: () -> Unit,
    onRecipeDetailsClick: (Recipe) -> Unit,
    onDevClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    startingSection: HomeSections = HomeSections.Feed,
) {
    val profile = remember { api.users.profile }.collectAsState(initial = null).value
    val (currentSection, setCurrentSection) = savedInstanceState { startingSection }

    Scaffold(topBar = {
        TupperdateTopBar(
            currentSection = currentSection,
            onSectionSelected = setCurrentSection,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    ) { innerPadding ->
        val innerModifier = modifier.padding(innerPadding)

        Crossfade(current = currentSection) { section ->
            when (section) {
                HomeSections.Feed -> Feed(
                    onNewRecipeClick = onNewRecipeClick,
                    onOpenRecipeClick = onRecipeDetailsClick,
                    onBack = onBack,
                    modifier = innerModifier,
                )
                HomeSections.Conversations -> Conversations(
                    onRecipeClick = {},
                    onProfileClick = {},
                    recipes = listOf(),
                    conversations = listOf(),
                    modifier = innerModifier,
                )
                HomeSections.Profile -> Profile(
                    userApi = api.users,
                    imagePicker = api.images,
                    profile = profile ?: api.users.emptyProfile,
                    onCloseClick = { setCurrentSection(HomeSections.Feed) },
                    onSignOutClick = {},
                    onDevClick = onDevClick,
                    modifier = innerModifier,
                )
            }
        }
    }
}

@Composable
private fun TupperdateTopBar(
    currentSection: HomeSections,
    onSectionSelected: (HomeSections) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(16.dp)
    ) {
        IconItem(
            asset = vectorResource(R.drawable.ic_home_messages),
            selected = currentSection == HomeSections.Conversations,
            onSelected = { onSectionSelected(HomeSections.Conversations) },
        )

        FeedItem(
            selected = currentSection == HomeSections.Feed,
            onSelected = { onSectionSelected(HomeSections.Feed) },
        )

        IconItem(
            asset = vectorResource(R.drawable.ic_home_accounts),
            selected = currentSection == HomeSections.Profile,
            onSelected = { onSectionSelected(HomeSections.Profile) },
        )
    }
}

@Composable
private fun IconItem(
    asset: ImageVector,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) Color.Smurf500 else Color.InactiveIcons

    Providers(AmbientContentColor provides color) {
        Icon(
            asset,
            modifier.selectable(selected = selected, onClick = onSelected),
        )
    }
}

@Composable
private fun FeedItem(
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Flamingo500)) { append("tupper ") }
        withStyle(SpanStyle(color = Color.Smurf500)) { append("• date") }
    }

    if (selected) {
        Text(
            text = title,
            style = TupperdateTypography.h6,
            modifier = modifier.selectable(selected = selected, onClick = onSelected),
        )
    } else {
        Providers(AmbientContentColor provides Color.InactiveIcons) {
            Icon(
                vectorResource(R.drawable.ic_home_cards),
                modifier.selectable(selected = selected, onClick = onSelected),
            )
        }
    }
}

enum class HomeSections {
    Conversations,
    Feed,
    Profile,
}

