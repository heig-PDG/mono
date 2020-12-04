package tupperdate.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import tupperdate.android.data.Graph
import tupperdate.android.data.api
import tupperdate.android.ui.theme.TupperdateTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = api()

        // TODO : Eventually switch to a dependency injection system.
        Graph.provide(this)

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
