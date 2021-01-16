package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import tupperdate.common.dto.*
import tupperdate.web.utils.authRequest
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import java.util.*
import kotlin.test.assertEquals

class ChatTest {
    @Test
    fun getEmptyChatList() {
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
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val newRecipe1 = NewRecipeDTO(
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            attributes = RecipeAttributesDTO(
                hasAllergens = false,
                vegetarian = true,
                warm = false,
            ),
            imageBase64 = null,
        )
        val newRecipe2 = NewRecipeDTO(
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            attributes = RecipeAttributesDTO(
                hasAllergens = true,
                vegetarian = false,
                warm = true,
            ),
            imageBase64 = null,
        )

        withTupperdateTestApplication {
            // Put user profiles
            registerUser(bot1Id, bot1Name)
            registerUser(bot2Id, bot2Name)

            // Post recipes
            postRecipe(bot1Id, newRecipe1)
            postRecipe(bot2Id, newRecipe2)

            // Get recipes id
            val recipeIdBot2 = getOldestRecipeId(bot1Id)
            val recipeIdBot1 = getOldestRecipeId(bot2Id)

            // Like recipes
            likeRecipe(bot1Id, recipeIdBot2)
            likeRecipe(bot2Id, recipeIdBot1)

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

    @Test
    fun testGetConv() {
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val newRecipe1 = NewRecipeDTO(
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            attributes = RecipeAttributesDTO(
                hasAllergens = false,
                vegetarian = true,
                warm = false,
            ),
            imageBase64 = null,
        )
        val newRecipe2 = NewRecipeDTO(
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            attributes = RecipeAttributesDTO(
                hasAllergens = true,
                vegetarian = false,
                warm = true,
            ),
            imageBase64 = null,
        )

        withTupperdateTestApplication {
            // Put user profiles
            registerUser(bot1Id, bot1Name)
            registerUser(bot2Id, bot2Name)

            // Post recipes
            postRecipe(bot1Id, newRecipe1)
            postRecipe(bot2Id, newRecipe2)

            // Get recipes id
            val recipeIdBot2 = getOldestRecipeId(bot1Id)
            val recipeIdBot1 = getOldestRecipeId(bot2Id)

            // Like recipes
            likeRecipe(bot1Id, recipeIdBot2)
            likeRecipe(bot2Id, recipeIdBot1)

            // Get conv as bot 1
            val conv1 = getConversation(bot1Id, bot2Id)
            assertEquals(bot2Id, conv1.userId)
            assertEquals(bot2Name, conv1.displayName)
            assertEquals(null, conv1.lastMessage)
            assertEquals(1, conv1.myRecipes.size)
            assertEquals(1, conv1.theirRecipes.size)
            assertEquals(recipeIdBot1, conv1.myRecipes[0].id)
            assertEquals(recipeIdBot2, conv1.theirRecipes[0].id)

            val conv2 = getConversation(bot2Id, bot1Id)
            assertEquals(bot1Id, conv2.userId)
            assertEquals(bot1Name, conv2.displayName)
            assertEquals(null, conv2.lastMessage)
            assertEquals(1, conv2.myRecipes.size)
            assertEquals(1, conv2.theirRecipes.size)
            assertEquals(recipeIdBot2, conv2.myRecipes[0].id)
            assertEquals(recipeIdBot1, conv2.theirRecipes[0].id)
        }
    }

    @Test
    fun testPostGetMessages() {
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val bot1MessageContent =
            MessageContentDTO(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        val bot2MessageContent =
            MessageContentDTO(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        val newRecipe1 = NewRecipeDTO(
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            attributes = RecipeAttributesDTO(
                hasAllergens = false,
                vegetarian = true,
                warm = false,
            ),
            imageBase64 = null,
        )
        val newRecipe2 = NewRecipeDTO(
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            attributes = RecipeAttributesDTO(
                hasAllergens = true,
                vegetarian = false,
                warm = true,
            ),
            imageBase64 = null,
        )

        withTupperdateTestApplication {
            // Put user profiles
            registerUser(bot1Id, bot1Name)
            registerUser(bot2Id, bot2Name)

            // Post recipes
            postRecipe(bot1Id, newRecipe1)
            postRecipe(bot2Id, newRecipe2)

            // Get recipes id
            val recipeIdBot2 = getOldestRecipeId(bot1Id)
            val recipeIdBot1 = getOldestRecipeId(bot2Id)

            // Like recipes
            likeRecipe(bot1Id, recipeIdBot2)
            likeRecipe(bot2Id, recipeIdBot1)

            // send messages
            sendMessage(bot1Id, bot2Id, bot1MessageContent)
            sendMessage(bot2Id, bot1Id, bot2MessageContent)


            // get messages
            val messagesBot1 = getMessages(bot1Id, bot2Id)
            val messagesBot2 = getMessages(bot2Id, bot1Id)

            assertEquals(2, messagesBot1.size)
            assertEquals(2, messagesBot2.size)

            assertEquals(bot1MessageContent.content, messagesBot1[0].content)
            assertEquals(bot2MessageContent.content, messagesBot2[1].content)

            assertEquals(bot1MessageContent.tempId, messagesBot1[0].tempId)
            assertEquals(bot2MessageContent.tempId, messagesBot2[1].tempId)
        }
    }

    private fun TestApplicationEngine.getConversation(
        botId: String,
        userId: String,
    ): ConversationDTO {
        handleRequest(HttpMethod.Get, "/chats/$userId/messages") {
            authRequest(botId)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            return Json.decodeFromString(response.content ?: "")
        }
    }

    private fun TestApplicationEngine.getMessages(
        botId: String,
        userId: String,
    ): List<MessageDTO> {
        handleRequest(HttpMethod.Get, "/chats/$userId/messages") {
            authRequest(botId)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            return Json.decodeFromString<List<MessageDTO>>(response.content ?: "")
        }
    }

    private fun TestApplicationEngine.sendMessage(
        botId: String,
        receiverId: String,
        message: MessageContentDTO
    ) {
        handleRequest(HttpMethod.Post, "/chats/$receiverId/message") {
            authRequest(botId)
            setBody(Json.encodeToString(message))
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }


    private fun TestApplicationEngine.likeRecipe(
        botId: String,
        recipeId: String
    ) {
        handleRequest(HttpMethod.Put, "/recipes/$recipeId/like") {
            authRequest(botId)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    private fun TestApplicationEngine.getOldestRecipeId(
        botId: String,
    ): String {
        handleRequest(HttpMethod.Get, "/recipes?count=1") {
            authRequest(botId)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")
            assertEquals(1, recipes.size)
            return recipes[0].id
        }
    }

    private fun TestApplicationEngine.postRecipe(
        botId: String,
        newRecipe: NewRecipeDTO
    ) {
        handleRequest(HttpMethod.Post, "/recipes") {
            authRequest(botId)
            jsonType()
            setBody(Json.encodeToString(newRecipe))
        }.apply { assertEquals(HttpStatusCode.OK, response.status()) }
    }

    private fun TestApplicationEngine.registerUser(
        botId: String,
        botName: String
    ) {
        handleRequest(HttpMethod.Put, "/users/$botId") {
            authRequest(botId)
            jsonType()
            val body = MyUserDTO(displayName = botName, imageBase64 = null)
            setBody(Json.encodeToString(body))
        }.apply { assertEquals(HttpStatusCode.OK, response.status()) }
    }
}
