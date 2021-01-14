package tupperdate.web.facade.chats

import org.koin.dsl.module

val KoinModuleFacadeChat = module {
    factory<ChatFacade> { ChatFacadeImpl(get()) }
}
