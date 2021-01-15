package tupperdate.web.legacy.routing.recipes

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import io.ktor.routing.*

fun Route.recipes(firebase: FirebaseApp) {
    route("/recipes") {
        // TODO (alex) : Provide the store differently ?
        recipesGet(FirestoreClient.getFirestore(firebase))
        recipesPut(FirestoreClient.getFirestore(firebase))
    }
}
