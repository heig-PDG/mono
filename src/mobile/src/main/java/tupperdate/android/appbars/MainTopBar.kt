package tupperdate.android.appbars

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.home.BarButton
import tupperdate.android.home.normalButtonSize
import tupperdate.android.ui.TupperdateTypography

@Composable
fun mainTopBar(
    modifier: Modifier = Modifier,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            BarButton(
                normalButtonSize,
                { onChatClick() },
                Color.Transparent,
                Color.Black,
                { Icon(vectorResource(id = R.drawable.ic_chat_24px)) }
            )
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "tupper.date",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TupperdateTypography.h5
                )
            }
            BarButton(
                normalButtonSize,
                { onProfileClick() },
                Color.Transparent,
                Color.Black, {
                    Icon(vectorResource(id = R.drawable.ic_account_circle_black_18dp))
                })
        }
    }

}
