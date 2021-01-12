package tupperdate.android.ui.navigation

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val KoinModuleUILoggedIn = module {
    viewModel { LoggedInViewModel(get()) }
}
