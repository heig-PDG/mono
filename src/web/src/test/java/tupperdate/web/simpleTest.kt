package tupperdate.web

import com.google.cloud.firestore.Firestore
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.ktor.ext.inject
import tupperdate.web.legacy.util.await
import kotlin.test.assertEquals


class SimpleTest {

    @Test
    fun testUnauthorized() {
        withTupperdateTestApplication {
            with(handleRequest(HttpMethod.Get, "/recipes")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun testEmulator() {
        withTupperdateTestApplication {
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
