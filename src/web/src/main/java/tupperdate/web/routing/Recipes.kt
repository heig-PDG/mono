package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.model.Chat
import tupperdate.common.model.Recipe
import tupperdate.common.model.RecipeAttributes
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.exceptions.BadRequestException
import tupperdate.web.exceptions.ForbiddenException
import tupperdate.web.exceptions.UnauthorizedException
import tupperdate.web.util.await

fun Route.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollectionGroup = firestore.collectionGroup("recipes")

        /**
         * Post a recipe as an authenticated user
         * @authenticated
         */
        post {
            try {
                val userCollection = firestore.collection("users")
                val userId = call.firebaseAuthPrincipal?.uid
                    ?: throw UnauthorizedException()

                val userDoc = userCollection.document(userId)

                // Generate document with auto-id
                val recipeDoc = userDoc.collection("recipes").document()

                // Get post data
                val json = call.receiveText()

                // Parse JSON (and add auto-generated id to it)
                val newRecipeDTO = Json.decodeFromString<NewRecipeDTO>(json)
                val recipe = Recipe(
                    id = recipeDoc.id,
                    title = newRecipeDTO.title,
                    timestamp = System.currentTimeMillis() / 1000,
                    attributes = RecipeAttributes(
                        hasAllergens = newRecipeDTO.attributes.hasAllergens,
                        vegetarian = newRecipeDTO.attributes.vegetarian,
                        warm = newRecipeDTO.attributes.warm,
                    )
                )

                recipeDoc.set(recipe)

                call.respond(HttpStatusCode.OK, Json.encodeToString(recipe))
            } catch (exception: UnauthorizedException) {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        /**
         * Get a number of recipes ordered by date added
         * @authenticated
         */
        get {
            try {
                val count = (call.request.queryParameters["count"]
                    ?: throw BadRequestException("query parameter \"count\" not found")).toInt()

                // TODO: Filter already liked/disliked recipes
                val recipes = recipeCollectionGroup
                    .orderBy("added")
                    .limit(count)
                    .get()
                    .await()
                    .toObjects(Recipe::class.java)
                    .toList()

                call.respond(HttpStatusCode.OK, Json.encodeToString(recipes))
            } catch (exception: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        /**
         * Post a like to a recipe as an authenticated user
         * @authenticated
         */
        put("like/{recipeId}") {
            try {
                val chatCollection = firestore.collection("chats")
                var userId1 = call.firebaseAuthPrincipal?.uid
                    ?: throw UnauthorizedException()

                val recipeId = call.parameters["recipeId"]
                    ?: throw BadRequestException("the request request body could not be parsed to a NewRecipe object")

                var userId2 =
                    recipeCollectionGroup
                        .whereEqualTo("id", recipeId)
                        .limit(1)
                        .get()
                        .await()
                        .documents[0]
                        .reference
                        .parent
                        .id

                if (userId1 == userId2) {
                    throw ForbiddenException("A user can't like its own recipe")
                }

                // Order userId1 and userId2 in alphanumerical order (1 is inferior to 2)
                var callerIsUser1 = true
                if (userId1 > userId2) {
                    callerIsUser1 = false

                    val temp = userId1
                    userId1 = userId2
                    userId2 = temp
                }

                // Get chat document or create it
                val chatDoc = chatCollection.document(userId1 + "_" + userId2)

                var chatObject = chatDoc.get().await().toObject(Chat::class.java)
                    ?: Chat(
                        id = chatDoc.id,
                        userId1 = userId1,
                        userId2 = userId2,
                        user1LikedRecipes = emptyList(),
                        user2LikedRecipes = emptyList(),
                    )

                // add liked recipe to correct list
                chatObject =
                    if (callerIsUser1) {
                        chatObject.copy(
                            user1LikedRecipes = (chatObject.user1LikedRecipes
                                ?: emptyList()) + recipeId
                        )
                    } else {
                        chatObject.copy(
                            user2LikedRecipes = (chatObject.user2LikedRecipes
                                ?: emptyList()) + recipeId
                        )
                    }

                chatDoc.set(chatObject)

                call.respond(HttpStatusCode.OK)
            } catch (exception: UnauthorizedException) {
                call.respond(HttpStatusCode.Unauthorized)
            } catch (exception: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: ForbiddenException) {
                call.respond(HttpStatusCode.Forbidden)
            }
        }

        /**
         * Post a dislike to a recipe as an authenticated user
         * @authenticated
         */
        put("dislike/{recipeId}") {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
}
