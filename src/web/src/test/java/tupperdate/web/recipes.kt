package tupperdate.web

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import tupperdate.common.dto.*
import tupperdate.web.utils.authRequest
import tupperdate.web.utils.botName
import tupperdate.web.utils.jsonType
import tupperdate.web.utils.koin.withTupperdateTestApplication
import tupperdate.web.utils.botId
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
                authRequest()
                jsonType()
                val body = MyUserDTO(displayName = botName, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipe
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest()
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
                authRequest()
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
    fun testGetSomeRecipe() {
        val title = "botRecipeTitle2"
        val description = "botRecipeDescription2"
        val hasAllergens = true
        val vegetarian = false
        val warm = false

        var recipeId = ""

        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$botId") {
                authRequest()
                jsonType()
                val body = MyUserDTO(displayName = botName, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipe
            handleRequest(HttpMethod.Post, "/recipes") {
                authRequest()
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
                authRequest()
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
                authRequest()
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
}
