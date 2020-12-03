package tupperdate.android.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier

@Composable
fun Home(
) {
    val (currentSection, setCurrentSection) = savedInstanceState { HomeSections.Feed }

    Scaffold(topBar = {
        TupperdateTopBar(
            onChatClick = { setCurrentSection(HomeSections.MessageList) },
            onProfileClick = { setCurrentSection(HomeSections.Profile) },
            onTitleClick = { setCurrentSection(HomeSections.Feed) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    ) { innerPadding ->
        Crossfade(current = currentSection) { section ->
            when (section) {
                HomeSections.Feed -> Feed()
                HomeSections.MessageList -> MessageList()
                HomeSections.Profile -> Profile()
            }
        }
    }
}

@Composable
fun Feed() {
    Text("Feed")
}

@Composable
fun MessageList() {
    Text("Message List")
}

@Composable
fun Profile() {
    Text("Profile")
}

private enum class HomeSections(
) {
    MessageList,
    Feed,
    Profile,
}
