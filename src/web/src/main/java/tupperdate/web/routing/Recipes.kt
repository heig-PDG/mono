package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import jdk.nashorn.internal.objects.Global.toObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.Recipe
import tupperdate.common.model.User
import tupperdate.web.autoId
import tupperdate.web.await

fun Routing.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollectionGroup = firestore.collectionGroup("recipes")

        /****************************************************************
         *                           GET                                *
         ****************************************************************/

        get("next/{numRecipes}") {
            val numRecipes = (call.parameters["numRecipes"] ?: "").toInt()
            val recipes = recipeCollectionGroup
                .orderBy("added")
                .limit(numRecipes)
                .get()
                .await()
                .toList()

            call.respond(recipes.map { toObject(Recipe::class.java) })
        }

        /****************************************************************
         *                            POST                              *
         ****************************************************************/

        // Temporary method to add recipes with a random user
        post {
            val userCollection = firestore.collection("users")
            val userId = call.parameters["userId"] ?: ""


            val userDocument = userCollection.document(userId)
            // Generate a new user document, temporarily
            val newRandomUserDoc = userCollection.document()
            val randomUser = User(
                id = newRandomUserDoc.id,
                displayName = "Random Person " + autoId(),
                phone = "Random phone " + autoId(),
                profilePictureUrl = "https://thispersondoesnotexist.com/",
            )
            // Assign the random user to new document
            newRandomUserDoc.set(randomUser)

            // Add a document in the subcollection "recipes"
            val newRecipeDoc = newRandomUserDoc
                .collection("recipes")
                .document()

            // Get post data
            val json = call.receiveText()

            // Parse JSON (and add auto-generated id to it)
            val recipe = Json.decodeFromString<Recipe>(json)
                .copy(
                    id = newRecipeDoc.id,
                    added = System.currentTimeMillis() / 1000,
                )

            newRecipeDoc.set(recipe)
        }
    }
}
