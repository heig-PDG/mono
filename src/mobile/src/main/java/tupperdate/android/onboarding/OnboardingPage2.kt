package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.oneButtonBottomBar
import tupperdate.android.appbars.onlyReturnTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.utils.checkReceveidCode

@Composable
fun OnboardingPage2(
    onButtonClick: () -> Unit,
    onReturnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val(code, setCode)= mutableStateOf("")

    TupperdateTheme {
        onlyReturnTopBar(onReturnClick)
        Column(
            modifier.padding(top = 64.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(text = "\n\nYou've got mail", style = TupperdateTypography.h5,
                modifier = Modifier.padding(bottom = 8.dp))

            codeInput(code = code, onCodeChanged = setCode)

            //this func will be implemented, but call won't changee
            checkReceveidCode(code.toInt())

            Row(modifier = Modifier.weight(1f)) {}
            oneButtonBottomBar(onButtonClick,
                "Let's go",
                "This app was created during a group project at HEIG-VD. Make sure to check it out on GitHub."
            )
        }
    }
}

@Composable
private fun codeInput(code: String, onCodeChanged :((String))->Unit, modifier: Modifier=Modifier)
{
    TextField(value = code,
        label = { Text(text = "Your code") },
        onValueChange = onCodeChanged,
        placeholder = { Text("2077") },
        keyboardType = KeyboardType.Number,
        modifier = modifier.fillMaxWidth(),)
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPage2Preview() {
    TupperdateTheme {
        OnboardingPage2(
            {}, {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}