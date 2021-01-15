package tupperdate.android.data.features.notifications

import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.notifications.impl.NotificationRepositoryImpl

val KoinModuleDataNotification = module {
    @OptIn(InternalDataApi::class)
    factory<NotificationRepository> { NotificationRepositoryImpl(get()) }
}
