package tupperdate.web.facade.chats

import io.ktor.http.*
import tupperdate.web.facade.accounts.AccountId
import tupperdate.web.facade.recipes.toRecipe
import tupperdate.web.model.Result
import tupperdate.web.model.accounts.Notification
import tupperdate.web.model.accounts.NotificationRepository
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.chats.Conversation
import tupperdate.web.model.chats.toModelNewMessage
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository
import tupperdate.web.model.recipes.RecipeRepository

class ChatFacadeImpl(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
    private val notificationRepository: NotificationRepository,
) : ChatFacade {

    override suspend fun readAll(
        user: User,
    ): Result<List<Chat>> {
        val convs: List<Conversation> = when (val res = chatRepository.readAll(user)) {
            is Result.Ok -> res.result
            else -> return res.map { listOf() }
        }

        val chats = convs.map { conv ->
            val chatUser =
                when (val res = userRepository.read(user = User(AccountId(conv.theirId)))) {
                    is Result.Ok -> res.result
                    else -> return res.map { listOf() }
                }
            val lastMessage =
                when (val res = chatRepository.readLastMessages(user = user, userId = conv.theirId)) {
                    is Result.Ok -> res.result
                    else -> return res.map { listOf() }
                }

            val myRecipes = conv.myRecipes.map {
                when (val res = recipeRepository.readOne(user, it)) {
                    is Result.Ok -> res.result
                    else -> return res.map { listOf() }
                }
            }
            val theirRecipes = conv.theirRecipes.map {
                when (val res = recipeRepository.readOne(User(AccountId(conv.theirId)), it)) {
                    is Result.Ok -> res.result
                    else -> return res.map { listOf() }
                }
            }

            return@map Chat(
                userId = conv.theirId,
                displayName = chatUser.displayName,
                picture = chatUser.displayPicture?.url,
                lastMessage = lastMessage.toMessage(),
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
        val conv: Conversation = when (val res = chatRepository.readOne(user, userId = userId)) {
            is Result.Ok -> res.result
            else -> return res.map { emptyChat() }
        }

        val chatUser = when (val res = userRepository.read(user = User(AccountId(conv.theirId)))) {
            is Result.Ok -> res.result
            else -> return res.map { emptyChat() }
        }
        val lastMessage =
            when (val res = chatRepository.readLastMessages(user = user, userId = conv.theirId)) {
                is Result.Ok -> res.result
                else -> return res.map { emptyChat() }
            }

        val myRecipes = conv.myRecipes.map {
            when (val res = recipeRepository.readOne(user, it)) {
                is Result.Ok -> res.result
                else -> return res.map { emptyChat() }
            }
        }
        val theirRecipes = conv.theirRecipes.map {
            when (val res = recipeRepository.readOne(User(AccountId(conv.theirId)), it)) {
                is Result.Ok -> res.result
                else -> return res.map { emptyChat() }
            }
        }

        return Result.Ok(
            Chat(
                userId = conv.theirId,
                displayName = chatUser.displayName,
                picture = chatUser.displayPicture?.url,
                lastMessage = lastMessage.toMessage(),
                myRecipes = myRecipes.map { it.toRecipe() },
                theirRecipes = theirRecipes.map { it.toRecipe() },
            )
        )
    }

    override suspend fun readMessages(
        user: User,
        userId: String,
    ): Result<List<Message>> {
        val modelMessages = chatRepository.readMessages(user = user, userId = userId)

        return modelMessages.map { it.map { modelMessage -> modelMessage.toMessage() } }
    }

    override suspend fun sendMessage(
        user: User,
        userId: String,
        newMessage: NewMessage,
    ): Result<Unit> {
        val result = chatRepository.sendMessage(
            user = user,
            userId = userId,
            newMessage = newMessage.toModelNewMessage()
        )

        // Let all the two clients sync their chats.
        if (result is Result.Ok) {
            notificationRepository.dispatch(
                Notification.ToUser.UserSyncOneConversation(userId, user.id.uid),
                Notification.ToUser.UserSyncOneConversation(user.id.uid, userId),
            )
        }

        return result
    }
}
