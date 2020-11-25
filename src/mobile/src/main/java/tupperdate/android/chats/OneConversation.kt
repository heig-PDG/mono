package tupperdate.android.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.ChatTopBar
import tupperdate.android.ui.Smurf100
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.R

@Composable
fun OneConversation(
    onReturnClick: () -> Unit,
    imageOther: Any,
    nameOther:String,
    otherOnline: String,
    modifier: Modifier = Modifier,
){
    Scaffold(
        topBar = {
            ChatTopBar(
                onReturnClick = onReturnClick,
                imageOther = imageOther,
                nameOther = nameOther,
                otherOnline = otherOnline
            )
        },
        bodyContent = {},
        bottomBar = { bottomBar() })
}

@Composable
private fun bottomBar(){
    Row(Modifier.fillMaxWidth().background(Color.Smurf100)){
        Row(Modifier.fillMaxWidth()
            .height(24.dp)
            .padding(16.dp)
            .background(Color.Transparent)){
            OutlinedTextField(modifier = Modifier.padding(end=10.dp),
                value = "",
                onValueChange = {})
            IconButton(onClick = {}) {
                Icon(asset = vectorResource(id = R.drawable.ic_chat_send))
            }
        }
    }
}

@Preview
@Composable
private fun OneConversationPreview(){
    TupperdateTheme {
        OneConversation(
            onReturnClick = {},
            imageOther = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg",
            nameOther = "Aloy",
            otherOnline = "online"
        )
    }
}
