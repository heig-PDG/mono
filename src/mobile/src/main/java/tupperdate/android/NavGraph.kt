package tupperdate.android

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tupperdate.android.utils.Navigator
import tupperdate.api.RecipeApi

sealed class Destination : Parcelable {
    //TODO delete this object once merged
    @Parcelize
    object MatchPopUpPretextPage : Destination()

    @Parcelize
    object NewRecipe : Destination()

    @Parcelize
    data class ViewRecipe(val recipe: RecipeApi.Recipe) : Destination()

    @Parcelize
    object Home : Destination()

    @Parcelize
    object BrandingPreview : Destination()

    @Parcelize
    object Onboarding : Destination()

    @Parcelize
    object OnboardingConfirmation : Destination()
}

class Action(private val navigator: Navigator<Destination>) {
    val matchPopUp: () -> Unit = {
        navigator.navigate(Destination.MatchPopUpPretextPage)
    }

    val newRecipe: () -> Unit = {
        navigator.navigate(Destination.NewRecipe)
    }

    val viewRecipe: (RecipeApi.Recipe) -> Unit = {
        navigator.navigate(Destination.ViewRecipe(it))
    }

    val home: () -> Unit = {
        navigator.navigate(Destination.Home)
    }

    val viewPreview: () -> Unit = {
        navigator.navigate(Destination.BrandingPreview)
    }

    val viewOnboarding: () -> Unit = {
        navigator.navigate(Destination.Onboarding)
    }

    val viewOnboardingConfirmation: () -> Unit = {
        navigator.navigate(Destination.OnboardingConfirmation)
    }

    val back: () -> Unit = {
        navigator.back()
    }
}
