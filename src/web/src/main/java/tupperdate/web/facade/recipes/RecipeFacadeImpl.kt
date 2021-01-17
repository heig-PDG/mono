package tupperdate.web.facade.recipes

import tupperdate.web.model.Result
import tupperdate.web.model.accounts.Notification
import tupperdate.web.model.accounts.NotificationRepository
import tupperdate.web.model.chats.ChatRepository
import tupperdate.web.model.chats.ModelNewChat
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository
import tupperdate.web.model.recipes.RecipeRepository
import tupperdate.web.model.recipes.toModelNewRecipe
import tupperdate.web.model.recipes.toModelPartRecipe

class RecipeFacadeImpl(
    private val recipes: RecipeRepository,
    private val users: UserRepository,
    private val chats: ChatRepository,
    private val notifications: NotificationRepository,
) : RecipeFacade {

    override suspend fun save(
        user: User,
        recipe: NewRecipe,
    ): Result<Unit> {
        val result = recipes.save(user, recipe.toModelNewRecipe())

        // Let all the currently active clients sync the stack.
        if (result is Result.Ok) notifications.dispatch(Notification.ToAll.UserSyncAllStackRecipes)

        return result
    }

    override suspend fun update(
        user: User,
        recipe: PartRecipe,
        recipeId: String,
    ): Result<Unit> {
        val recipeBefore = when (val res = recipes.readOne(user, recipeId)) {
            is Result.Ok -> res.result
            else -> return res.map {}
        }

        if (user.id.uid != recipeBefore.userId) {
            return Result.Forbidden()
        }

        val result = recipes.update(user, recipe.toModelPartRecipe(recipeId = recipeId))

        // Let the recipe owner sync his recipe
        if (result is Result.Ok) notifications.dispatch(
            Notification.ToUser.UserSyncOneRecipe(
                user.id.uid,
                recipeId
            )
        )

        return Result.Ok(Unit)
    }

    override suspend fun readOwn(
        user: User,
    ): Result<List<Recipe>> {
        return recipes.readOwn(user).map { it.map { that -> that.toRecipe() } }
    }

    override suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<Recipe>> {
        val lastSeenRecipe = when (val time = users.read(user).map { it.lastSeenRecipe }) {
            is Result.Ok -> time.result
            else -> 0
        }

        return recipes.readAll(user, count, lastSeenRecipe)
            .map { it.map { that -> that.toRecipe() } }
    }

    override suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<Recipe> {
        return recipes.readOne(user, recipeId).map { it.toRecipe() }
    }

    override suspend fun like(
        user: User,
        recipeId: String,
    ): Result<Unit> {
        val recipe = when (val recipe = recipes.readOne(user, recipeId)) {
            is Result.Ok -> recipe.result
            else -> return recipe.map {}
        }

        // A user can't like his own recipe
        if (user.id.uid == recipe.userId) return Result.Forbidden()

        val smallerId = minOf(user.id.uid, recipe.userId)
        val greaterId = maxOf(user.id.uid, recipe.userId)
        val callerLike = if (recipe.userId < user.id.uid) "user1Recipes" else "user2Recipes"

        val chatId = smallerId + "_" + greaterId
        val newChat = ModelNewChat(id = chatId, userId1 = smallerId, userId2 = greaterId)
        // Set base data but don't touch likes arrays
        val res1 = chats.saveNewChat(newChat)
        if (chats.saveNewChat(newChat) !is Result.Ok) { return res1 }

        // Append recipe likes to correct array
        val res2 = chats.updateLikes(chatId, mapOf(callerLike to recipeId))
        if (res2 !is Result.Ok) {
            return res2
        }

        // Notify both users that their conversations might have changed.
        notifications.dispatch(
            Notification.ToUser.UserSyncAllConversations(smallerId),
            Notification.ToUser.UserSyncAllConversations(greaterId),
        )

        // set recipe as seen
        val result = users.updateLastSeenRecipe(user, recipe.timestamp)

        // Notify the user that his stack might have changed.
        if (result is Result.Ok) notifications.dispatch(
            Notification.ToUser.UserSyncAllStackRecipes(user.id.uid),
        )

        return result
    }

    override suspend fun dislike(
        user: User,
        recipeId: String,
    ): Result<Unit> {
        val time = when (val recipe = recipes.readOne(user, recipeId).map { it.timestamp }) {
            is Result.Ok -> recipe.result
            else -> return recipe.map { }
        }

        return users.updateLastSeenRecipe(user, time)
    }
}
