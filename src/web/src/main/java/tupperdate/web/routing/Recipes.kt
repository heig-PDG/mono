package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.Chat
import tupperdate.common.model.Recipe
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.util.await

fun Routing.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollectionGroup = firestore.collectionGroup("recipes")

        /**
         * Get a number of recipes ordered by date added
         * @param: numRecipes as an int in the endpoint path
         */
        get("{numRecipes}") {
            val numRecipes = (call.parameters["numRecipes"] ?: "").toInt()
            val recipes = recipeCollectionGroup
                .orderBy("added")
                .limit(numRecipes)
                .get()
                .await()
                .toObjects(Recipe::class.java)
                .toList()

            call.respond(HttpStatusCode.OK, recipes)
        }

        /**
         * Post a recipe for the authenticated user
         */
        authenticate {
            post {
                val userCollection = firestore.collection("users")
                val userId = call.firebaseAuthPrincipal?.uid ?: ""


                val userDoc = userCollection.document(userId)

                // Generate document with auto-id
                val recipeDoc = userDoc.collection("recipes").document()

                // Get post data
                val json = call.receiveText()

                // Parse JSON (and add auto-generated id to it)
                val recipe = Json.decodeFromString<Recipe>(json)
                    .copy(
                        id = recipeDoc.id,
                        added = System.currentTimeMillis() / 1000,
                    )

                recipeDoc.set(recipe)
                call.respond(HttpStatusCode.OK)
            }
        }

        /**
         * Post a like to a recipe for the authenticated user
         */
        authenticate {
            post("like/{recipeId}") {
                try {
                    val chatCollection = firestore.collection("chats")
                    val recipeId = call.parameters["recipeId"] ?: ""
                    var callerIsUser1 = true

                    var userId1 = call.firebaseAuthPrincipal?.uid
                        ?: throw IllegalArgumentException("Null uid provided is authenticated call")
                    var userId2 =
                        recipeCollectionGroup
                            .whereIn("id", listOf(recipeId))
                            .limit(1)
                            .get()
                            .await()
                            .documents[0]
                            .reference
                            .parent
                            .id

                    if (userId1 == userId2) {
                        throw java.lang.IllegalStateException("A user can't like its own recipe")
                    }

                    // Order userId1 and userId2 in alphanumerical order (1 is inferior to 2)
                    if (userId1 > userId2) {
                        val temp = userId1
                        userId1 = userId2
                        userId2 = temp
                        callerIsUser1 = false
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

                    // add liked recipe
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

                } catch (exception: IllegalArgumentException) {
                    call.respond(HttpStatusCode.Unauthorized)
                } catch (exception: IllegalStateException) {
                    call.respond(HttpStatusCode.NotAcceptable)
                }
            }
        }

        authenticate {
            post("dislike/{recipeId}") {
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}
