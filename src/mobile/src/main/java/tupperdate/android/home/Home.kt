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
    user: AuthenticationApi.User?,
    modifier: Modifier = Modifier,
) {
    if (user == null) {
        HomeDisconnected(
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
    modifier: Modifier,
) {
    Column(
        modifier.padding(10.dp)
    ) {
        Text(text = "Hi there. You cannot click on anything")
    }
}

@Composable
private fun HomeConnected(
    onButtonClick: () -> Unit,
    user: AuthenticationApi.User,
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
    val user = AuthenticationApi.User(
        "john@appleseed.com",
        "John Appleseed",
        null
    )

    TupperdateTheme {
        Home(
            onButtonClick = {},
            user = user,
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}
