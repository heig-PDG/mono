package tupperdate.android.data

import androidx.room.Room
import androidx.work.WorkManager
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import io.ktor.util.*
import org.koin.dsl.module
import tupperdate.android.data.features.auth.firebase.firebase
import tupperdate.android.data.room.TupperdateDatabase
import kotlinx.serialization.json.Json as JsonConfig

@InternalDataApi
@OptIn(KtorExperimentalAPI::class)
private val KoinKtorModule = module {
    single {
        HttpClient {
            install(Auth) { firebase() }
            install(JsonFeature) {
                serializer = KotlinxSerializer(JsonConfig {
                    encodeDefaults = false
                })
            }
            install(Logging) {
                level = LogLevel.HEADERS
                logger = Logger.SIMPLE
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.tupperdate.me"
                }
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
