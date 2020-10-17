package tupperdate.android

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import tupperdate.android.ui.BrandingPreview
import tupperdate.android.ui.TupperdateTheme
import tupperdate.api.Api

@Composable
fun TupperdateApp(
    api: Api,
    backDispatcher: OnBackPressedDispatcher
) {
    TupperdateTheme {
        BrandingPreview()
    }
}