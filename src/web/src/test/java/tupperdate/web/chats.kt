package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import tupperdate.common.dto.*
import tupperdate.web.utils.authRequest
import tupperdate.web.utils.botId
import tupperdate.web.utils.botName
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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

    @Test
    fun testMatchGetChats() {
        val bot1Id = "bot1Id"
        val bot2Id = "bot2Id"
        val bot1Name = "bot1Name"
        val bot2Name = "bot2Name"
        val title1 = "botRecipeTitle1"
        val title2 = "botRecipeTitle2"
        val description1 = "botRecipeDescription1"
        val description2 = "botRecipeDescription2"
        val hasAllergens1 = false
        val hasAllergens2 = false
        val vegetarian1 = false
        val vegetarian2 = false
        val warm1 = true
        val warm2 = true

        var recipeIdBot1 = ""
        var recipeIdBot2 = ""

        withTupperdateTestApplication {
            // Put user profile 1
            handleRequest(HttpMethod.Put, "/users/$bot1Id") {
                authRequest(bot1Id)
                jsonType()
                val body = MyUserDTO(displayName = bot1Name, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Put user profile 2
            handleRequest(HttpMethod.Put, "/users/$bot2Id") {
                authRequest(bot2Id)
                jsonType()
                val body = MyUserDTO(displayName = bot2Name, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipe as bot 1
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(bot1Id)
                jsonType()
                val body = NewRecipeDTO(
                    title = title1,
                    description = description1,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens1,
                        vegetarian = vegetarian1,
                        warm = warm1,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipe as bot 2
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(bot2Id)
                jsonType()
                val body = NewRecipeDTO(
                    title = title2,
                    description = description2,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens2,
                        vegetarian = vegetarian2,
                        warm = warm2,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipes as bot 1
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot1Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")
                assertEquals(1, recipes.size)
                recipeIdBot2 = recipes[0].id
            }

            // Get recipes as bot 2
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")
                assertEquals(1, recipes.size)
                recipeIdBot1 = recipes[0].id
            }

            // Like recipe as bot 1
            handleRequest(HttpMethod.Put, "/recipes/$recipeIdBot2/like") {
                authRequest(bot1Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Like recipe as bot 2
            handleRequest(HttpMethod.Put, "/recipes/$recipeIdBot1/like") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Get chats as bot 1
            handleRequest(HttpMethod.Get, "/chats") {
                authRequest(bot1Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val chats = Json.decodeFromString<List<ConversationDTO>>(response.content ?: "")
                assertEquals(1, chats.size)
                val chat = chats[0]
                assertEquals(bot2Id, chat.userId)
                assertEquals(bot2Name, chat.displayName)
                assertEquals(null, chat.lastMessage)
                assertEquals(1, chat.myRecipes.size)
                assertEquals(1, chat.theirRecipes.size)
                assertEquals(recipeIdBot1, chat.myRecipes[0].id)
                assertEquals(recipeIdBot2, chat.theirRecipes[0].id)
            }

            // Get chats as bot 2
            handleRequest(HttpMethod.Get, "/chats") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val chats = Json.decodeFromString<List<ConversationDTO>>(response.content ?: "")
                assertEquals(1, chats.size)
                val chat = chats[0]
                assertEquals(bot1Id, chat.userId)
                assertEquals(bot1Name, chat.displayName)
                assertEquals(null, chat.lastMessage)
                assertEquals(1, chat.myRecipes.size)
                assertEquals(1, chat.theirRecipes.size)
                assertEquals(recipeIdBot2, chat.myRecipes[0].id)
                assertEquals(recipeIdBot1, chat.theirRecipes[0].id)
            }
        }
    }


}
