package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.auth.firebaseAuthPrincipal
import tupperdate.web.util.await

fun Route.recipesPost(store: Firestore) = post {
    val uid = requireNotNull(call.firebaseAuthPrincipal?.uid)
    val doc = store.collection("users").document(uid).collection("recipes").document()
    val dto = call.receive<NewRecipeDTO>()
    val now = System.currentTimeMillis() / 1000

    // TODO : Use a some better representations for the model.
    doc.set(
        mapOf(
            "id" to doc.id,
            "title" to dto.title,
            "description" to dto.description,
            "timestamp" to now,
            "attributes" to mapOf(
                "hasAllergens" to dto.attributes.hasAllergens,
                "vegetarian" to dto.attributes.vegetarian,
                "warm" to dto.attributes.warm,
            )
        )
    ).await()

    call.respond(
        HttpStatusCode.OK, RecipeDTO(
            id = doc.id,
            title = dto.title,
            timestamp = now,
            attributes = RecipeAttributesDTO(
                hasAllergens = dto.attributes.hasAllergens,
                vegetarian = dto.attributes.vegetarian,
                warm = dto.attributes.warm,
            ),
            description = dto.description,
            picture = "https://thispersondoesnotexist.com/image",
        )
    )
}
