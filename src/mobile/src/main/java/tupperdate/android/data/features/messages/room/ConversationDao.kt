package tupperdate.android.data.features.messages.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.android.data.features.messages.ConversationIdentifier
import tupperdate.common.dto.ConversationDTO

@Dao
@InternalDataApi
abstract class ConversationDao {

    @Query("SELECT * FROM conversations")
    abstract fun conversations(): Flow<List<ConversationEntity>>

    /**
     * Retrieves a specific conversation.
     */
    @Query("SELECT * FROM conversations WHERE conversations.id = :forUid")
    abstract fun conversation(forUid: FirebaseUid): Flow<ConversationEntity?>

    @Query("DELETE FROM conversations WHERE conversations.id = :forUid")
    abstract suspend fun conversationDelete(forUid: FirebaseUid)

    @Query("DELETE FROM conversations")
    abstract suspend fun conversationDeleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun conversationReplace(conv: ConversationEntity)

    /**
     * Fetches a [ConversationEntity] once.
     */
    @Deprecated(message = "Reserved for internal use.")
    @Query("SELECT * FROM conversations WHERE conversations.id = :forUid")
    abstract suspend fun conversationOnce(forUid: FirebaseUid): ConversationEntity?

    /**
     * Saves a single [ConversationDTO] in the database. This will take care of inserting all of the
     * recipes that they might have, as well as cleaning eventually previous recipes that we might
     * have had at our disposal.
     */
    @Transaction
    open suspend fun conversationSave(conv: ConversationDTO) {
        val existing = conversationOnce(conv.userId)
        // 1. Replace all the recipes for this conversation, no matter what.
        conversationRecipesDeleteAll(conv.userId)
        val entity = ConversationEntity(
            identifier = conv.userId,
            name = conv.displayName,
            picture = conv.picture,
            previewBody = conv.lastMessage?.content,
            previewTimestamp = conv.lastMessage?.timestamp,
            accepted = existing?.accepted ?: false,
        )
        conversationReplace(entity)
        for (recipe in conv.myRecipes) {
            conversationRecipeReplace(
                ConversationRecipeEntity(
                    convId = conv.userId,
                    recipeId = recipe.id,
                    recipeBelongsToThem = false,
                    recipePicture = recipe.picture,
                    recipeName = recipe.title,
                )
            )
        }
        for (recipe in conv.theirRecipes) {
            conversationRecipeReplace(
                ConversationRecipeEntity(
                    convId = conv.userId,
                    recipeId = recipe.id,
                    recipeBelongsToThem = true,
                    recipePicture = recipe.picture,
                    recipeName = recipe.title,
                )
            )
        }
    }

    @Insert
    abstract suspend fun conversationRecipeReplace(recipe: ConversationRecipeEntity)

    // Conversation recipes.
    @Query("DELETE FROM conversationsRecipes WHERE conversationsRecipes.convId = :forUid")
    abstract suspend fun conversationRecipesDeleteAll(forUid: ConversationIdentifier)

    /**
     * Accepts the conversation with the given id.
     */
    @Query("UPDATE conversations SET accepted = 1 WHERE conversations.id = :forUid")
    abstract suspend fun accept(forUid: String)
}
