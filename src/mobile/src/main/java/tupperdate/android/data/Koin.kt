package tupperdate.android.data

import androidx.room.Room
import androidx.work.WorkManager
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.dsl.module
import tupperdate.android.data.features.auth.impl.firebase
import tupperdate.android.data.room.TupperdateDatabase

@InternalDataApi
private val KoinKtorModule = module {
    factory {
        HttpClient {
            install(Auth) { firebase() }
            install(JsonFeature)
            defaultRequest {
                // TODO : Use HTTPS.
                host = "api.tupperdate.me"
                port = 80
                contentType(ContentType.parse("application/json"))
            }
        }
    }
}

@InternalDataApi
private val KoinWorkManagerModule = module {
    factory { WorkManager.getInstance(get()) }
}

@InternalDataApi
private val KoinRoomModule = module {
    single {
        Room.databaseBuilder(get(), TupperdateDatabase::class.java, "db.sqlite")
            .fallbackToDestructiveMigration()
            .build()
    }
}

// Offer all the required modules.

@OptIn(InternalDataApi::class)
val KoinDataApiModule = KoinKtorModule + KoinWorkManagerModule + KoinRoomModule