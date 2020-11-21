package tupperdate.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.api

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = api()
        setContent {
            TupperdateTheme {
                TupperdateApp(
                    api = api,
                    backDispatcher = onBackPressedDispatcher,
                )
            }
        }
    }
}
