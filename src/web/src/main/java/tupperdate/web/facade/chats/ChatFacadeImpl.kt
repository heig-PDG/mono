package tupperdate.web.facade.chats

import io.ktor.http.*
import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.recipes.toRecipe
import tupperdate.web.model.Result
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.chats.Conversation
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository
import tupperdate.web.model.recipes.RecipeRepository

class ChatFacadeImpl(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
) : ChatFacade {

    override suspend fun readAll(
        user: User,
    ): Result<List<Chat>> {
        val convs: List<Conversation> = when(val res = chatRepository.readAll(user)) {
            is Result.Ok -> res.result
            else -> return Result.BadServer()
        }

        val chats = convs.map { conv ->
            val chatUser = when(val res = userRepository.read(user = User(AccountId(conv.theirId)))) {
                is Result.Ok -> res.result
                else -> return Result.BadServer()
            }
            val lastMessage = when(val res = chatRepository.readMessages(user = user, otherUserId = conv.theirId)) {
                is Result.Ok -> res.result
                else -> return Result.BadServer()
            }.getOrNull(0)
            val myRecipes = conv.myRecipes.map {
                when(val res = recipeRepository.readOne(user, it)) {
                    is Result.Ok -> res.result
                    else -> return Result.BadServer()
                }
            }
            val theirRecipes = conv.theirRecipes.map {
                when(val res = recipeRepository.readOne(User(AccountId(conv.theirId)), it)) {
                    is Result.Ok -> res.result
                    else -> return Result.BadServer()
                }
            }

            return@map Chat(
                userId = conv.theirId,
                displayName = chatUser.displayName,
                picture = chatUser.displayPicture?.url,
                lastMessage = lastMessage?.toMessage(),
                myRecipes = myRecipes.map { it.toRecipe() },
                theirRecipes = theirRecipes.map { it.toRecipe() },
            )

        }

        return Result.Ok(chats)
    }

    override suspend fun readOne(
        user: User,
        userId: String,
    ): Result<Chat> {
        val conv: Conversation = when(val res = chatRepository.readOne(user, userId = userId)) {
            is Result.Ok -> res.result
            else -> return Result.BadServer()
        }

        val chatUser = when(val res = userRepository.read(user = User(AccountId(conv.theirId)))) {
            is Result.Ok -> res.result
            else -> return Result.BadServer()
        }
        val lastMessage = when(val res = chatRepository.readMessages(user = user, otherUserId = conv.theirId)) {
            is Result.Ok -> res.result
            else -> return Result.BadServer()
        }.getOrNull(0)

        val myRecipes = conv.myRecipes.map {
            when(val res = recipeRepository.readOne(user, it)) {
                is Result.Ok -> res.result
                else -> return Result.BadServer()
            }
        }
        val theirRecipes = conv.theirRecipes.map {
            when(val res = recipeRepository.readOne(User(AccountId(conv.theirId)), it)) {
                is Result.Ok -> res.result
                else -> return Result.BadServer()
            }
        }

        return Result.Ok(
            Chat(
                userId = conv.theirId,
                displayName = chatUser.displayName,
                picture = chatUser.displayPicture?.url,
                lastMessage = lastMessage?.toMessage(),
                myRecipes = myRecipes.map { it.toRecipe() },
                theirRecipes = theirRecipes.map { it.toRecipe() },
            )
        )
    }

    override suspend fun readMessages(
        user: User,
        otherUserId: String,
    ): Result<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        user: User,
        otherUserId: String,
        content: String,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}
