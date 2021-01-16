package tupperdate.web.model.accounts.fcm

import org.koin.dsl.module
import tupperdate.web.model.accounts.NotificationRepository

val KoinModuleModelNotificationFcm = module {
    factory<NotificationRepository> { FcmNotificationRepository(get(), get()) }
}
