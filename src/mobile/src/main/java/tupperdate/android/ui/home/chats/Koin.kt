package tupperdate.android.ui.home.chats

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val KoinModuleUIMessages = module {
    viewModel { ConversationsViewModel(get()) }
    viewModel { (id: String) -> OneConversationViewModel(id = id, get()) }
}
