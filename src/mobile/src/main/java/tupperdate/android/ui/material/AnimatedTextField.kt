package tupperdate.android.ui.material

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.ui.Flamingo700
import tupperdate.android.ui.Smurf700
import tupperdate.android.ui.TupperdateTheme

private val Active = ColorPropKey()

private val Transition = transitionDefinition<Int> {
    state(0) {
        this[Active] = Color.Smurf700
    }
    state(100) {
        this[Active] = Color.Flamingo700
    }

    transition {
        Active using repeatable(
            animation = tween(7 * 1000),
            iterations = AnimationConstants.Infinite,
            repeatMode = RepeatMode.Reverse,
        )
    }
}

@Composable
fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val colors = transition(Transition, initState = 0, toState = 100)

    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        keyboardType = keyboardType,
        activeColor = colors[Active],
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimatedTextFieldPreview() {
    TupperdateTheme {
        AnimatedTextField(
            "",
            {},
            Modifier.background(Color.White)
                .padding(16.dp),
            label = { Text(text = "Your phone number") },
        )
    }
}