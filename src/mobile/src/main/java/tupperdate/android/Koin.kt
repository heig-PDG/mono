package tupperdate.android

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import tupperdate.android.data.KoinDataApiModule
import tupperdate.android.data.features.auth.KoinAuthenticationModule
import tupperdate.android.data.features.recipe.KoinRecipeModule
import tupperdate.android.ui.home.KoinHomeModule
import tupperdate.android.ui.onboarding.KoinModuleUIOnboardingModule

fun Tupperdate.startKoin() = org.koin.core.context.startKoin {
    androidContext(this@startKoin)
    androidLogger()

    // Data modules.
    modules(KoinDataApiModule)
    modules(KoinAuthenticationModule)

    modules(KoinRecipeModule)

    // Application modules.
    modules(KoinHomeModule)
    modules(KoinModuleUIOnboardingModule)
}
