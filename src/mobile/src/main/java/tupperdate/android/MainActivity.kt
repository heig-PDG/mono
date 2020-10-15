package tupperdate.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.platform.setContent
import tupperdate.android.ui.BrandingPreview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.api

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = api()
        setContent {

            TupperdateTheme {
                // TODO : Make the preview private once we no longer want it.
                BrandingPreview()
            }
        }
    }
}