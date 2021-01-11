package tupperdate.android.data.features.messages

import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.messages.impl.MessagesRepositoryImpl

val KoinModuleDataMessages = module {
    factory<MessagesRepository> {
        @OptIn(InternalDataApi::class)
        MessagesRepositoryImpl(get(), get(), get())
    }
}
