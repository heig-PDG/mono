package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.utils.authRequest
import tupperdate.web.utils.botId
import tupperdate.web.utils.botName
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RecipesTest {

    @Test
    fun testPostGetOwnRecipe() {
        val title = "botRecipeTitle1"
        val description = "botRecipeDescription1"
        val hasAllergens = true
        val vegetarian = false
        val warm = false

        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$botId") {
                authRequest(botId)
                jsonType()
                val body = MyUserDTO(displayName = botName, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipe
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(botId)
                jsonType()
                val body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipe
            handleRequest(HttpMethod.Get, "/recipes/own") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertEquals(recipe.title, title)
                assertEquals(recipe.description, description)
                assertEquals(recipe.userId, botId)
                assertEquals(recipe.attributes.hasAllergens, hasAllergens)
                assertEquals(recipe.attributes.vegetarian, vegetarian)
                assertEquals(recipe.attributes.warm, warm)
                assertNotEquals(recipe.timestamp, 0)
            }
        }
    }

    @Test
    fun testPostGetSomeRecipe() {
        val title = "botRecipeTitle2"
        val description = "botRecipeDescription2"
        val hasAllergens = true
        val vegetarian = false
        val warm = false

        var recipeId = ""

        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$botId") {
                authRequest(botId)
                jsonType()
                val body = MyUserDTO(displayName = botName, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipe
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(botId)
                jsonType()
                val body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipe
            handleRequest(HttpMethod.Get, "/recipes/own") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertEquals(recipe.title, title)
                assertEquals(recipe.description, description)
                assertEquals(recipe.userId, botId)
                assertEquals(recipe.attributes.hasAllergens, hasAllergens)
                assertEquals(recipe.attributes.vegetarian, vegetarian)
                assertEquals(recipe.attributes.warm, warm)
                assertNotEquals(recipe.timestamp, 0)

                recipeId = recipe.id
            }

            // Get this particular recipe
            handleRequest(HttpMethod.Get, "/recipes/$recipeId") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<RecipeDTO>(response.content ?: "")
                assertEquals(recipe.title, title)
                assertEquals(recipe.description, description)
                assertEquals(recipe.userId, botId)
                assertEquals(recipe.attributes.hasAllergens, hasAllergens)
                assertEquals(recipe.attributes.vegetarian, vegetarian)
                assertEquals(recipe.attributes.warm, warm)
                assertNotEquals(recipe.timestamp, 0)
            }
        }
    }

    @Test
    fun testPostGetAllRecipesFromOthers() {
        val bot1Id = "bot1Id"
        val bot2Id = "bot2Id"
        val bot1Name = "bot1Name"
        val bot2Name = "bot2Name"
        val title = "botRecipeTitle3"
        val description = "botRecipeDescription3"
        val hasAllergens = false
        val vegetarian = false
        val warm = true

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
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipes as bot 2
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertEquals(recipe.title, title)
                assertEquals(recipe.description, description)
                assertEquals(recipe.userId, bot1Id)
                assertEquals(recipe.attributes.hasAllergens, hasAllergens)
                assertEquals(recipe.attributes.vegetarian, vegetarian)
                assertEquals(recipe.attributes.warm, warm)
                assertNotEquals(recipe.timestamp, 0)
            }
        }
    }

    @Test
    fun testPostGetAllLikeRecipe() {
        val bot1Id = "bot1Id"
        val bot2Id = "bot2Id"
        val bot1Name = "bot1Name"
        val bot2Name = "bot2Name"
        val title = "botRecipeTitle4"
        val description = "botRecipeDescription4"
        val hasAllergens = false
        val vegetarian = false
        val warm = true

        var recipeId = ""

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

            // Post recipes as bot 1
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(bot1Id)
                jsonType()
                val body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(bot1Id)
                jsonType()
                val body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipes as bot 2
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]

                recipeId = recipe.id
            }

            // Dislike recipe as bot 2
            handleRequest(HttpMethod.Put, "/recipes/$recipeId/like") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Get recipes as bot 2 again, and check previous one is not visible anymore
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertNotEquals(recipeId, recipe.id)
            }

        }
    }

    @Test
    fun testPostGetAllDislikeRecipe() {
        val bot1Id = "bot1Id"
        val bot2Id = "bot2Id"
        val bot1Name = "bot1Name"
        val bot2Name = "bot2Name"
        val title = "botRecipeTitle4"
        val description = "botRecipeDescription4"
        val hasAllergens = false
        val vegetarian = false
        val warm = true

        var recipeId = ""

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

            // Post recipes as bot 1
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(bot1Id)
                jsonType()
                val body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest(bot1Id)
                jsonType()
                val body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergens,
                        vegetarian = vegetarian,
                        warm = warm,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipes as bot 2
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]

                recipeId = recipe.id
            }

            // Dislike recipe as bot 2
            handleRequest(HttpMethod.Put, "/recipes/$recipeId/dislike") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Get recipes as bot 2 again, and check previous one is not visible anymore
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertNotEquals(recipeId, recipe.id)
            }

        }
    }
}
