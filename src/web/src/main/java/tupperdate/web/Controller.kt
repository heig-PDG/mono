package tupperdate.web

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject
import tupperdate.common.dto.*
import tupperdate.web.facade.accounts.AccountFacade
import tupperdate.web.facade.chats.ChatFacade
import tupperdate.web.facade.chats.toConversationDTO
import tupperdate.web.facade.chats.toMessageDTO
import tupperdate.web.facade.chats.toNewMessage
import tupperdate.web.facade.profiles.*
import tupperdate.web.facade.recipes.RecipeFacade
import tupperdate.web.facade.recipes.toNewRecipe
import tupperdate.web.facade.recipes.toRecipeDTO
import tupperdate.web.model.Result
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User
import tupperdate.web.utils.statusException
import tupperdate.web.utils.tupperdateAuthPrincipal

fun Route.endpoints() {
    val profileFacade by this.inject<ProfileFacade>()

    // Notifications
    route("/notifications") {
        post {
            profileFacade.register(
                user = requireUser(),
                token = requireBody<NewNotificationTokenDTO>().toNotificationToken()
            ).let { respond(it) }
        }
    }

    // Accounts
    route("/accounts") {
        val accountFacade by this.inject<AccountFacade>()
        post("logout") {
            respond(accountFacade.logout(user = requireUser()))
        }
    }

    // Users
    route("/users") {
        put("/{userId}") {
            profileFacade.save(
                user = requireUser(),
                profileId = requireParam("userId"),
                profile = requireBody<MyUserDTO>().toNewProfile()
            ).let { respond(it) }
        }

        patch("/{userId}") {
            profileFacade.update(
                user = requireUser(),
                profileId = requireParam("userId"),
                partProfile = requireBody<MyUserPartDTO>().toPartProfile()
            ).let { respond(it) }
        }

        get("/{userId}") {
            profileFacade.read(
                user = requireUser(),
                profileId = requireParam("userId")
            ).map(Profile::toUserDTO).let { respond(it) }
        }
    }

    // Recipes
    route("/recipes") {
        val recipeFacade by this.inject<RecipeFacade>()

        post {
            recipeFacade.save(
                user = requireUser(),
                recipe = requireBody<NewRecipeDTO>().toNewRecipe(),
            ).let { respond(it) }
        }

        get("/own") {
            recipeFacade.readOwn(user = requireUser())
                .map { it.map { recipe -> recipe.toRecipeDTO() } }
                .let { respond(it) }
        }

        get("/{identifier}") {
            recipeFacade.readOne(user = requireUser(), recipeId = requireParam("identifier"))
                .map { it.toRecipeDTO() }.let { respond(it) }
        }

        get {
            recipeFacade.readAll(
                user = requireUser(),
                count = requireParam("count").toIntOrNull()
                    ?: throw statusException(HttpStatusCode.BadRequest)
            ).map { it.map { recipe -> recipe.toRecipeDTO() } }.let { respond(it) }
        }

        put("/{recipeId}/like") {
            respond(recipeFacade.like(user = requireUser(), recipeId = requireParam("recipeId")))
        }

        put("/{recipeId}/dislike") {
            respond(recipeFacade.dislike(user = requireUser(), recipeId = requireParam("recipeId")))
        }
    }

    // Chats
    route("/chats") {
        val chatFacade by this.inject<ChatFacade>()
        get {
            chatFacade.readAll(user = requireUser())
                .map { it.map { chat -> chat.toConversationDTO() } }.let { respond(it) }
        }

        get("/{userId}") {
            chatFacade.readOne(user = requireUser(), userId = requireParam("userId"))
                .map { it.toConversationDTO() }.let { respond(it) }
        }

        get("/{userId}/messages") {
            chatFacade.readMessages(user = requireUser(), userId = requireParam("userId"))
                .map { it.map { message -> message.toMessageDTO() } }.let { respond(it) }
        }

        post("/{userId}/messages") {
            val newMessage = requireBody<MessageContentDTO>()
            chatFacade.sendMessage(
                user = requireUser(),
                userId = requireParam("userId"),
                newMessage = newMessage.toNewMessage(),
            ).let { respond(it) }
        }
    }
}

private suspend inline fun <T> PipelineContext<Unit, ApplicationCall>.respond(result: Result<T>) {
    val (body, code) = when (result) {
        is Result.Ok -> result.result to HttpStatusCode.OK
        is Result.Unauthorized -> result.message to HttpStatusCode.Unauthorized
        is Result.Forbidden -> result.message to HttpStatusCode.Forbidden
        is Result.BadInput -> result.message to HttpStatusCode.BadRequest
        is Result.NotFound -> result.message to HttpStatusCode.NotFound
        is Result.MissingData -> result.message to HttpStatusCode.InternalServerError
        is Result.BadServer -> result.message to HttpStatusCode.InternalServerError
    }

    call.respond(code, body ?: "Info not provided by server")
}

private suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.requireBody(): T =
    call.receive()

private fun PipelineContext<Unit, ApplicationCall>.requireParam(name: String): String =
    call.parameters[name] ?: statusException(HttpStatusCode.BadRequest)

private fun PipelineContext<Unit, ApplicationCall>.requireUser(): User =
    call.tupperdateAuthPrincipal?.uid?.let(::User) ?: statusException(HttpStatusCode.Unauthorized)
