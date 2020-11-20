package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tupperdate.common.model.Recipe
import tupperdate.common.model.User
import tupperdate.web.autoId
import tupperdate.web.await

fun Routing.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollectionGroup = firestore.collectionGroup("recipes")

        get("next") {
            /*var recipe: QueryDocumentSnapshot?
            do {
                recipe = recipeCollection
                    .whereGreaterThan("id", autoId())
                    .orderBy("id")
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .getOrNull(0)
            } while (recipe == null)*/

            val recipe = recipeCollectionGroup
                .limit(1)
                .get()
                .await()
                .documents[0]

            call.respond(recipe.toObject(Recipe::class.java))
        }

        // Temporary method to add recipes with a random user
        post {
            val usersCollection = firestore.collection("users")

            // Generate a new user document
            val newRandomUserDoc = usersCollection.document()
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
            val recipe = Json.decodeFromString<Recipe>(json).copy(id = newRecipeDoc.id)

            newRecipeDoc.set(recipe)
        }
    }
}
