package tupperdate.web

import com.google.cloud.firestore.Firestore
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ktor.ext.inject
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.legacy.util.*
import tupperdate.web.model.impl.firestore.KoinModuleModelFirebase
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore
import kotlin.test.*

fun testApp(test: TestApplicationEngine.() -> Unit) {
    withTestApplication {
        stopKoin() // Need to stop koin and restart after other tests
        startKoin {
            modules(KoinModuleModelFirebase)
            modules(KoinModuleFacadeProfile)
            modules(KoinModuleModelUsersFirestore)
        }

        test() // Run you application
    }
}


class SimpleTest {

    @Test
    fun testUnauthorized() = testApp {
        application.installServer()
        with(handleRequest(HttpMethod.Get, "/recipes")) {
            assertEquals(HttpStatusCode.Unauthorized, response.status())
        }
    }

    @Test
    fun testEmulator() = testApp {
        application.installServer()
        val firestore by application.inject<Firestore>()

        val doc = firestore.collection("emulatorOnlyDeleteMe").document("uniqueId")
        runBlocking {
            doc.set(mapOf("displayName" to "Emulatron3000")).await()
            val name = doc.get().await().get("displayName") as String
            assertEquals(name, "Emulatron3000")
        }
    }
}
