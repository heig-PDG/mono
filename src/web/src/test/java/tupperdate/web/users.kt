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
import tupperdate.web.utils.botName
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import tupperdate.web.utils.botId
import kotlin.test.assertEquals


class UsersTest {

    @Test
    fun testPutUser() {
        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$botId") {
                authRequest()
                jsonType()
                val json = Json.encodeToString(MyUserDTO(displayName = botName, imageBase64 = null))
                println(json)
                setBody(json)
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get user profile
            handleRequest(HttpMethod.Get, "/users/$botId") {
                authRequest()
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val user = Json.decodeFromString<UserDTO>(response.content ?: "")
                assertEquals(user.id, botId)
                assertEquals(user.displayName, botName)
            }
        }
    }
}
