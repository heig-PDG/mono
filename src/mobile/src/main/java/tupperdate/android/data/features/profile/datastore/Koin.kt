package tupperdate.android.data.features.profile.datastore

import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.profile.RestrictionsRepository

val KoinModuleDataRestrictions = module {
    @OptIn(InternalDataApi::class)
    single<RestrictionsRepository> { DatastoreRestrictionsRepository(get()) }
}
