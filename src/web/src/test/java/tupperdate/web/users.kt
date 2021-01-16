package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.UserDTO
import tupperdate.web.utils.authRequest
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import java.util.*
import kotlin.test.assertEquals


class UsersTest {

    @Test
    fun testPutGetUser() {
        val botId = UUID.randomUUID().toString()
        val botName = UUID.randomUUID().toString()
        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$botId") {
                authRequest(botId)
                jsonType()
                val body = MyUserDTO(displayName = botName, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get user profile
            handleRequest(HttpMethod.Get, "/users/$botId") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val user = Json.decodeFromString<UserDTO>(response.content ?: "")
                assertEquals(user.id, botId)
                assertEquals(user.displayName, botName)
            }
        }
    }
}
