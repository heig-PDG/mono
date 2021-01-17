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
import utils.OptionalProperty
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RecipesTest {

    @Test
    fun testPostGetOwnRecipe() {
        val botId = UUID.randomUUID().toString()
        val botName = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
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
                assertEquals(title, recipe.title)
                assertEquals(description, recipe.description)
                assertEquals(botId, recipe.userId)
                assertEquals(hasAllergens, recipe.attributes.hasAllergens)
                assertEquals(vegetarian, recipe.attributes.vegetarian)
                assertEquals(warm, recipe.attributes.warm)
                assertNotEquals(0, recipe.timestamp)
            }
        }
    }

    @Test
    fun testPostPatchGetOwnRecipe() {
        val botId = UUID.randomUUID().toString()
        val botName = UUID.randomUUID().toString()
        val titleBefore = UUID.randomUUID().toString()
        val titleAfter = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
        val hasAllergensBefore = true
        val hasAllergensAfter = false
        val vegetarian = false
        val warmBefore = false
        val warmAfter = true

        var recipeId: String

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
                    title = titleBefore,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        hasAllergens = hasAllergensBefore,
                        vegetarian = vegetarian,
                        warm = warmBefore,
                    ),
                    imageBase64 = null,
                )
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get recipe id
            handleRequest(HttpMethod.Get, "/recipes/own") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                recipeId = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0].id
            }

            // Patch recipe
            handleRequest(HttpMethod.Patch, "/recipes/$recipeId") {
                authRequest(botId)
                jsonType()
                val body = RecipePartDTO(
                    title = OptionalProperty.Provided(titleAfter),
                    attributes = RecipeAttributesPartDTO(
                        hasAllergens = OptionalProperty.Provided(hasAllergensAfter),
                        warm = OptionalProperty.Provided(warmAfter),
                    ),
                )
                setBody(Json.encodeToString(body))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Get recipe
            handleRequest(HttpMethod.Get, "/recipes/own") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertEquals(recipeId, recipe.id)
                assertEquals(titleAfter, recipe.title)
                assertEquals(description, recipe.description)
                assertEquals(botId, recipe.userId)
                assertEquals(hasAllergensAfter, recipe.attributes.hasAllergens)
                assertEquals(vegetarian, recipe.attributes.vegetarian)
                assertEquals(warmAfter, recipe.attributes.warm)
                assertNotEquals(0, recipe.timestamp)
            }
        }
    }

    @Test
    fun testPostGetSomeRecipe() {
        val botId = UUID.randomUUID().toString()
        val botName = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
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
                assertEquals(title, recipe.title)
                assertEquals(description, recipe.description)
                assertEquals(botId, recipe.userId)
                assertEquals(hasAllergens, recipe.attributes.hasAllergens)
                assertEquals(vegetarian, recipe.attributes.vegetarian)
                assertEquals(warm, recipe.attributes.warm)
                assertNotEquals(0, recipe.timestamp)

                recipeId = recipe.id
            }

            // Get this particular recipe
            handleRequest(HttpMethod.Get, "/recipes/$recipeId") {
                authRequest(botId)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<RecipeDTO>(response.content ?: "")
                assertEquals(title, recipe.title)
                assertEquals(description, recipe.description)
                assertEquals(botId, recipe.userId)
                assertEquals(hasAllergens, recipe.attributes.hasAllergens)
                assertEquals(vegetarian, recipe.attributes.vegetarian)
                assertEquals(warm, recipe.attributes.warm)
                assertNotEquals(0, recipe.timestamp)
            }
        }
    }

    @Test
    fun testPostGetAllRecipesFromOthers() {
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
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
                assertEquals(title, recipe.title)
                assertEquals(description, recipe.description)
                assertEquals(bot1Id, recipe.userId)
                assertEquals(hasAllergens, recipe.attributes.hasAllergens)
                assertEquals(vegetarian, recipe.attributes.vegetarian)
                assertEquals(warm, recipe.attributes.warm)
                assertNotEquals(0, recipe.timestamp)
            }
        }
    }

    @Test
    fun testPostGetAllLikeRecipe() {
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
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
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")

                assertEquals(1, recipes.size)
                recipeId = recipes[0].id
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
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
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

    @Test
    fun testPost3Recipeslike1Recipe() {
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
        val hasAllergens = false
        val vegetarian = false
        val warm = true

        var recipeIds: MutableList<String> = mutableListOf()

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
            handleRequest(HttpMethod.Get, "/recipes?count=3") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")

                recipeIds.add(recipes[0].id)
                recipeIds.add(recipes[1].id)
                recipeIds.add(recipes[2].id)
            }

            // Like recipe 0 as bot 2
            handleRequest(HttpMethod.Put, "/recipes/${recipeIds[0]}/like") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Dislike recipe 1 as bot 2
            handleRequest(HttpMethod.Put, "/recipes/${recipeIds[1]}/dislike") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Get recipes as bot 2 again, and check previous ones are not visible anymore
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipe = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")[0]
                assertEquals(recipe.id, recipeIds[2])
            }
        }
    }

    @Test
    fun testGetAllRecipesFromOthersNoRecipesPosted() {
        val bot1Id = UUID.randomUUID().toString()
        val bot2Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val bot2Name = UUID.randomUUID().toString()

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

            // Get recipes as bot 2
            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot2Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")
                assertEquals(0, recipes.size)
            }
        }
    }

    @Test
    fun testGetOwnNoRecipesPosted() {
        val bot1Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()

        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$bot1Id") {
                authRequest(bot1Id)
                jsonType()
                val body = MyUserDTO(displayName = bot1Name, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Get own recipes
            handleRequest(HttpMethod.Get, "/recipes/own") {
                authRequest(bot1Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")
                assertEquals(0, recipes.size)
            }
        }
    }

    @Test
    fun testGetAllRecipesNoSeeOwnRecipes() {
        val bot1Id = UUID.randomUUID().toString()
        val bot1Name = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()
        val description = UUID.randomUUID().toString()
        val hasAllergens = false
        val vegetarian = false
        val warm = true

        withTupperdateTestApplication {
            // Put user profile
            handleRequest(HttpMethod.Put, "/users/$bot1Id") {
                authRequest(bot1Id)
                jsonType()
                val body = MyUserDTO(displayName = bot1Name, imageBase64 = null)
                setBody(Json.encodeToString(body))
            }.apply { assertEquals(HttpStatusCode.OK, response.status()) }

            // Post recipes as bot
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

            handleRequest(HttpMethod.Get, "/recipes?count=1") {
                authRequest(bot1Id)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val recipes = Json.decodeFromString<List<RecipeDTO>>(response.content ?: "")
                assertEquals(0, recipes.size)
            }
        }
    }
}
