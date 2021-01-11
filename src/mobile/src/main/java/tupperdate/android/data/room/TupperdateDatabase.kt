package tupperdate.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.room.ProfileDao
import tupperdate.android.data.features.auth.room.ProfileEntity
import tupperdate.android.data.features.messages.room.ConversationEntity
import tupperdate.android.data.features.messages.room.MessageDao
import tupperdate.android.data.features.messages.room.MessageEntity
import tupperdate.android.data.features.messages.room.PendingMessageEntity
import tupperdate.android.data.features.recipe.room.PendingNewRecipeEntity
import tupperdate.android.data.features.recipe.room.PendingRateRecipeEntity
import tupperdate.android.data.features.recipe.room.RecipeDao
import tupperdate.android.data.features.recipe.room.RecipeEntity

@Database(
    entities = [
        // Profiles API.
        ProfileEntity::class,
        // Recipes API.
        RecipeEntity::class,
        // Messaging API.
        ConversationEntity::class,
        MessageEntity::class,
        // Changes to be sent to the server.
        PendingNewRecipeEntity::class,
        PendingRateRecipeEntity::class,
        PendingMessageEntity::class,
    ],
    version = 1,
    exportSchema = false, // we're not yet interested in schema migrations
)
@InternalDataApi
abstract class TupperdateDatabase : RoomDatabase() {
    abstract fun messages(): MessageDao
    abstract fun profiles(): ProfileDao
    abstract fun recipes(): RecipeDao
}
