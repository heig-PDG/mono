package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import tupperdate.web.util.*
import kotlin.test.*

class SimpleTest {
    @Test
    fun testUnauthorized() {
        // Initialise FirebaseApp
        val firebase = initialiseApp()

        withTestApplication {
            application.installServer(firebase)

            with(handleRequest(HttpMethod.Get, "/recipes")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}
