package tupperdate.android

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tupperdate.android.utils.Navigator

sealed class Destination : Parcelable {
    @Parcelize
    object Home : Destination()

    @Parcelize
    object BrandingPreview : Destination()

    @Parcelize
    object Onboarding : Destination()

    @Parcelize
    object OnboardingPage2 : Destination()
}

class Action(navigator: Navigator<Destination>) {
    val home: () -> Unit = {
        navigator.navigate(Destination.Home)
    }

    val viewPreview: () -> Unit = {
        navigator.navigate(Destination.BrandingPreview)
    }

    val viewOnboarding: () -> Unit = {
        navigator.navigate(Destination.Onboarding)
    }

    val viewOnboardingPage2: () -> Unit = {
        navigator.navigate(Destination.OnboardingPage2)
    }
}