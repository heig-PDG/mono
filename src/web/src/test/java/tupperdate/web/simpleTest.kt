package tupperdate.web

import com.google.cloud.firestore.Firestore
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.ktor.ext.inject
import tupperdate.web.koin.withTupperdateTestApplication
import tupperdate.web.legacy.util.await
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class SimpleTest {

    @Test
    fun testUnauthorized() {
        withTupperdateTestApplication {
            with(handleRequest(HttpMethod.Get, "/recipes?count=1") {
                addHeader(HttpHeaders.Authorization, "Bearer Tokythetoken")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testEmulator() {
        withTupperdateTestApplication {
            val firestore by application.inject<Firestore>()

            val doc = firestore.collection("zzz_emulatorTestDontDeleteMe").document("test")
            runBlocking {
                val value = doc.get().await()
                assertNotEquals(value["name"], "firestore")

                doc.set(mapOf("emulator" to "Emulatron3000")).await()
                val name = doc.get().await().get("emulator") as String
                assertEquals(name, "Emulatron3000")
            }
        }
    }
}
