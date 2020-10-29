package tupperdate.android.home

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.AuthenticationApi

@Composable
fun Home(
    onButtonClick: () -> Unit,
    user: AuthenticationApi.Profile?,
    modifier: Modifier = Modifier,
) {
    if (user == null) {
        HomeDisconnected(
            onButtonClick,
            modifier,
        )
    } else {
        HomeConnected(
            onButtonClick,
            user,
            modifier,
        )
    }
}

@Composable
private fun HomeDisconnected(
    onButtonClick: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier.padding(10.dp)
    ) {
        Text(text = "Hi there.")
        Button(onClick = onButtonClick) {
            Text("Go to onboarding")
        }
    }
}

@Composable
private fun HomeConnected(
    onButtonClick: () -> Unit,
    user: AuthenticationApi.Profile,
    modifier: Modifier,
) {
    Column(
        modifier.padding(10.dp)
    ) {
        Text(text = "Hi there ${user.displayName}")
        Button(onClick = onButtonClick) {
            Text("View Branding")
        }
    }
}

@Preview
@Composable
private fun HomeDisconnectPreview() {
    TupperdateTheme {
        Home(
            onButtonClick = {},
            user = null,
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun HomeConnectedPreview() {
    val profile = AuthenticationApi.Profile(
        displayName = "John Appleseed",
        phoneNumber = "144",
        profileImageUrl = null,
    )

    TupperdateTheme {
        Home(
            onButtonClick = {},
            user = profile,
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}
