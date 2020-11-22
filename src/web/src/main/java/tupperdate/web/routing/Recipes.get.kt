package tupperdate.web.routing

import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.dto.RecipeAttributesDTO
import tupperdate.common.dto.RecipeDTO
import tupperdate.web.exceptions.BadRequestException
import tupperdate.web.util.await

fun Route.recipesGet(store: Firestore) = get {
    // Extract query params.
    val countParam = call.request.queryParameters["count"] ?: throw BadRequestException()
    val count = countParam.toIntOrNull() ?: throw BadRequestException()

    // TODO: Filter already liked/disliked recipes
    val retrieved = store.collectionGroup("recipes").orderBy("timestamp").limit(count).get().await()
    val mapped = retrieved.map {
        RecipeDTO(
            id = it.id,
            title = it["title"] as String,
            description = it["description"] as String,
            picture = "https://thispersondoesnotexist.com/image",
            timestamp = it["timestamp"] as Long,
            attributes = RecipeAttributesDTO(
                hasAllergens = it[FieldPath.of("attributes", "hasAllergens")] as Boolean,
                warm = it[FieldPath.of("attributes", "warm")] as Boolean,
                vegetarian = it[FieldPath.of("attributes", "vegetarian")] as Boolean,
            )
        )
    }
    call.respond(HttpStatusCode.OK, mapped)
}
