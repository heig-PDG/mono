package tupperdate.web

import io.ktor.application.*
import io.ktor.server.testing.*
import org.koin.ktor.ext.Koin
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.model.impl.firestore.KoinModuleModelFirebase
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore

fun <R> withTupperdateTestApplication(engine: TestApplicationEngine.() -> R): R =
    withTestApplication {
        application.install(Koin) {
            modules(KoinModuleModelFirebase)
            modules(KoinModuleFacadeProfile)
            modules(KoinModuleModelUsersFirestore)
        }
        application.installServer()
        engine()
    }
