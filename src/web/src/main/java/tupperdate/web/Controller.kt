package tupperdate.web

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import tupperdate.common.dto.MyUserDTO
import tupperdate.web.facade.profiles.ProfileFacade
import tupperdate.web.facade.profiles.toNewProfile
import tupperdate.web.legacy.auth.firebaseAuthPrincipal
import tupperdate.web.model.BadInputException
import tupperdate.web.model.Result
import tupperdate.web.model.UnauthorizedException
import tupperdate.web.model.profiles.User

fun Route.endpoints() {
    route("/users") {
        val facade by this.inject<ProfileFacade>()

        put("{userId}") {
            facade.save(
                user = call.user(),
                profileId = call.param("userId"),
                profile = call.receive<MyUserDTO>().toNewProfile()
            ).response()?.let { call.respond(it) }
        }

        get("{userId}") {
            facade.read(
                user = call.user(),
                profileId = call.param("userId")
            ).response()?.let { call.respond(it) }
        }
    }
}

fun Result<*>.response(): Any? = when (this) {
    is Result.Ok<*> -> this.result
    is Result.Forbidden<*> -> HttpStatusCode.Forbidden
    is Result.BadInput<*> -> HttpStatusCode.BadRequest
    is Result.NotFound<*> -> HttpStatusCode.NotFound
    is Result.MissingData<*> -> HttpStatusCode.InternalServerError
    is Result.BadServer<*> -> HttpStatusCode.InternalServerError
}

private fun ApplicationCall.param(parameter: String): String =
    parameters[parameter] ?: throw BadInputException()

private fun ApplicationCall.user(): User {
    val id = firebaseAuthPrincipal?.uid ?: throw UnauthorizedException()
    val data = request.header("Authorization") ?: throw UnauthorizedException()
    if (!data.startsWith("Bearer")) throw UnauthorizedException()
    val token = data.replaceFirst("Bearer", "").trim()

    return User(
        token = token,
        id = id,
    )
}
