package tupperdate.android.ui.theme.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed


fun Modifier.multiClick(triggerAmount: Int, action: () -> Unit) = composed {
    val (count, setCount) = remember { mutableStateOf(0) }
    if (count < triggerAmount) {
        this then clickable(onClick = { setCount(count + 1) })
    } else {
        this then clickable(onClick = action)
    }
}
