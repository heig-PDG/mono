package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tupperdate.common.model.Recipe
import tupperdate.web.await
import kotlin.random.Random

fun autoId(): String {
    // Alphanumeric characters
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    var autoId = "";
    val targetLength = 20;
    while (autoId.length < targetLength) {
        val rand = Random.nextInt(chars.length)
        autoId += chars[rand]
    }

    return autoId;
}

fun Routing.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollection = firestore.collection("recipes/")

        get("next") {
            var recipe: QueryDocumentSnapshot?
            do {
                recipe = recipeCollection
                    .whereGreaterThan("id", autoId())
                    .orderBy("id")
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .getOrNull(0)
            } while (recipe == null)

            val json = Json.encodeToString(recipe.toObject(Recipe::class.java))

            call.response.headers.append("Content-Type", "application/json", false)
            call.respond(json)
        }
    }
}
