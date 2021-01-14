package tupperdate.web

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject
import tupperdate.web.legacy.util.*
import kotlin.test.*

// Initialise FirebaseApp

class SimpleTest {

    @Test
    fun testUnauthorized() {
        withTestApplication {
            application.installServer()

            with(handleRequest(HttpMethod.Get, "/recipes")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun testEmulator() {
        withTestApplication {
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
}
