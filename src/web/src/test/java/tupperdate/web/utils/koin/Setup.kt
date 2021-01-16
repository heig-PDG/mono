package tupperdate.web.utils.koin

import io.ktor.application.*
import io.ktor.server.testing.*
import org.koin.ktor.ext.Koin
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.facade.recipes.KoinModuleFacadeRecipe
import tupperdate.web.installServer
import tupperdate.web.model.accounts.firestore.KoinModuleModelAuthFirebase
import tupperdate.web.model.chats.firestore.KoinModuleModelChatsFirestore
import tupperdate.web.model.impl.firestore.KoinModuleModelFirebase
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore
import tupperdate.web.model.recipes.firestore.KoinModuleModelRecipesFirestore

fun <R> withTupperdateTestApplication(engine: TestApplicationEngine.() -> R): R =
    withTestApplication {
        application.install(Koin) {
            modules(KoinModuleModelFirebase)
            modules(KoinModuleModelAuthFirebase)

            modules(KoinModuleModelUsersFirestore)
            modules(KoinModuleModelChatsFirestore)
            modules(KoinModuleModelRecipesFirestore)

            modules(KoinModuleFacadeAccountMock)
            modules(KoinModuleRepositoryPhoneMock)
            modules(KoinModuleNotificationMock)

            modules(KoinModuleFacadeProfile)
            modules(KoinModuleFacadeRecipe)

        }
        application.installServer()
        engine()
    }
