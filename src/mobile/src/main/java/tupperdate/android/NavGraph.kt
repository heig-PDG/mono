package tupperdate.android

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tupperdate.android.utils.Navigator

sealed class Destination : Parcelable {
    @Parcelize
    object Home : Destination()

    @Parcelize
    object BrandingPreview : Destination()
}

class Action(navigator: Navigator<Destination>) {
    val home : () -> Unit = {
        navigator.navigate(Destination.Home)
    }

    val viewPreview: () -> Unit = {
        navigator.navigate(Destination.BrandingPreview)
    }
}