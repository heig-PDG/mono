package tupperdate.android.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import tupperdate.android.data.features.recipe.KoinRecipeModule
import tupperdate.android.data.ktor.KoinKtorModule
import tupperdate.android.data.room.KoinRoomModule
import tupperdate.android.ui.home.KoinHomeModule

/**
 * Creates a new Koin module for the root activity of our application. This will be necessary to
 * provide the proper injected instances of Context and lifecycle-related components.
 */
fun MainActivity.koin() = org.koin.dsl.module {

    // Context injection.
    single<Context> { this@koin } binds arrayOf(
        AppCompatActivity::class,
        ComponentActivity::class,
    )

    // CoroutineScope injection.
    single<CoroutineScope> { get<AppCompatActivity>().lifecycleScope }
}

/**
 * Actually performs injection. This will load the different modules of the app, and bind them
 * together such that dependencies get resolved.
 */
fun MainActivity.injectAllTheThings() = startKoin {
    // Load the activity as a module.
    modules(koin())

    // Data modules.
    modules(KoinKtorModule)
    modules(KoinRoomModule)
    modules(KoinRecipeModule)

    // Application modules.
    modules(KoinHomeModule)
}
