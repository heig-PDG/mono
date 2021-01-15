package tupperdate.android.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.getViewModel
import tupperdate.android.R
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.home.chats.Conversations
import tupperdate.android.ui.home.feed.Feed
import tupperdate.android.ui.home.profile.Profile
import tupperdate.android.ui.theme.Flamingo500
import tupperdate.android.ui.theme.InactiveIcons
import tupperdate.android.ui.theme.Smurf500
import tupperdate.android.ui.theme.TupperdateTypography

@Composable
@OptIn(ExperimentalCoroutinesApi::class)
fun Home(
    onNewRecipeClick: () -> Unit,
    onRecipeDetailsClick: (Recipe) -> Unit,
    onConversationClick: (ConversationIdentifier) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<HomeViewModel>()
    val section by viewModel.section.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TupperdateTopBar(
                currentSection = section,
                onSectionSelected = viewModel::onSectionSelected,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        bodyContent = { innerPadding ->
            Crossfade(section) { section ->
                when (section) {
                    HomeSection.Feed -> Feed(
                        onNewRecipeClick = onNewRecipeClick,
                        onOpenRecipeClick = onRecipeDetailsClick,
                        onBack = onBack,
                        modifier = Modifier.padding(innerPadding),
                    )
                    HomeSection.Conversations ->
                        Conversations(
                            onConversationClick = onConversationClick,
                            modifier = Modifier.padding(innerPadding),
                        )
                    HomeSection.Profile -> Profile(
                        onNewRecipeClick = onNewRecipeClick,
                        onOwnRecipeClick = onRecipeDetailsClick,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    )
}

@Composable
private fun TupperdateTopBar(
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.preferredHeight(56.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {
        IconItem(
            asset = vectorResource(R.drawable.ic_home_messages),
            selected = currentSection == HomeSection.Conversations,
            onSelected = { onSectionSelected(HomeSection.Conversations) },
        )

        FeedItem(
            selected = currentSection == HomeSection.Feed,
            onSelected = { onSectionSelected(HomeSection.Feed) },
        )

        IconItem(
            asset = vectorResource(R.drawable.ic_home_accounts),
            selected = currentSection == HomeSection.Profile,
            onSelected = { onSectionSelected(HomeSection.Profile) },
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
    val color = animate(if (selected) Color.Smurf500 else Color.InactiveIcons)
    Providers(AmbientContentColor provides color) {
        IconButton(onSelected, modifier) {
            Icon(asset)
        }
    }
}

private val TupperdateTitle = buildAnnotatedString {
    withStyle(SpanStyle(color = Color.Flamingo500)) { append("tupper ") }
    withStyle(SpanStyle(color = Color.Smurf500)) { append("• date") }
}

@Composable
private fun FeedItem(
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(selected) {
        if (selected) {
            Text(
                text = TupperdateTitle,
                style = TupperdateTypography.h6,
                modifier = modifier
                    .clip(RoundedCornerShape(50))
                    .clickable(onClick = onSelected)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            )
        } else {
            IconItem(
                asset = vectorResource(R.drawable.ic_home_cards),
                selected = selected,
                onSelected = onSelected,
            )
        }
    }
}

enum class HomeSection {
    Conversations,
    Feed,
    Profile,
}

