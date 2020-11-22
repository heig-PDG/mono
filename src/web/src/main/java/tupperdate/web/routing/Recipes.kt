package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.Recipe
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.util.await

fun Routing.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollectionGroup = firestore.collectionGroup("recipes")

        /****************************************************************
         *                           GET                                *
         ****************************************************************/

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
                .toList()

            call.respond(recipes.map { it.toObject(Recipe::class.java) })
        }

        /****************************************************************
         *                            POST                              *
         ****************************************************************/

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
            }
        }
    }
}
