package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import tupperdate.common.dto.ConversationDTO
import tupperdate.common.dto.MyUserDTO
import tupperdate.web.utils.authRequest
import tupperdate.web.utils.botId
import tupperdate.web.utils.botName
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import kotlin.test.assertEquals

class ChatTest {
    @Test
    fun getEmptyChatList() {
        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$botId") {
                authRequest(botId)
                jsonType()
                val body = MyUserDTO(displayName = botName, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get chat list
            handleRequest(HttpMethod.Get, "/recipes/own") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val chats = Json.decodeFromString<List<ConversationDTO>>(response.content ?: "")
                assertEquals(0, chats.size)
            }
        }
    }


}
