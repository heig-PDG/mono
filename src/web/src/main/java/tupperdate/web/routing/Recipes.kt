package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.routing.*

fun Route.recipes(store: Firestore) {
    route("/recipes") {
        // TODO (alex) : Provide the store differently ?
        recipesPost(store)
        recipesGet(store)
        recipesPut(store)
    }
}
