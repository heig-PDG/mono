package tupperdate.android

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import tupperdate.android.data.KoinDataApiModule
import tupperdate.android.data.features.auth.KoinModuleDataAuthentication
import tupperdate.android.data.features.recipe.KoinModuleDataRecipe
import tupperdate.android.ui.home.KoinModuleUIHome
import tupperdate.android.ui.onboarding.KoinModuleUIOnboarding

/**
 * The [Application] class, which is loaded when the user launches our program.
 */
class Tupperdate : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
}

/**
 * Performs dependency injection by providing all the required dependencies. This should only be
 * called once.
 */
fun Tupperdate.startKoin() = org.koin.core.context.startKoin {
    androidContext(this@startKoin)
    androidLogger()

    // Data modules.
    modules(KoinDataApiModule)
    modules(KoinModuleDataAuthentication)

    modules(KoinModuleDataRecipe)

    // Application modules.
    modules(KoinModuleUIHome)
    modules(KoinModuleUIOnboarding)
}
