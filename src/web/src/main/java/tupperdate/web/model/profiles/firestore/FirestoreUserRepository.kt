package tupperdate.web.model.profiles.firestore

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.google.firebase.cloud.StorageClient
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.apache.commons.codec.binary.Base64
import tupperdate.common.dto.notifications.Notifications
import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.Result.*
import tupperdate.web.model.profiles.*
import tupperdate.web.utils.await
import java.util.*
import java.util.concurrent.TimeUnit

class FirestoreUserRepository(
    private val store: Firestore,
    private val storage: StorageClient,
    private val messaging: FirebaseMessaging,
) : UserRepository {

    override suspend fun save(
        user: ModelNewUser,
    ): Result<Unit> = coroutineScope {
        val doc = store.collection("users").document(user.identifier)

        val id = UUID.randomUUID().toString()
        val bytes = user.displayPicture?.let { Base64.decodeBase64(it.encoded) }
        var picture: String? = null

        if (bytes != null) {
            val fileName = "$id.jpg"
            val blob = storage.bucket().create(
                fileName,
                bytes.inputStream(),
                ContentType.Image.JPEG.contentType,
            )
            // TODO: Find alternative to timeout
            val url = blob.signUrl(365, TimeUnit.DAYS)
            picture = url.toString()
        }

        val firestoreUser = user.toFirestoreUser(
            picture = picture?.let(::PictureUrl),
        )

        try {
            doc.set(
                firestoreUser,
                SetOptions.mergeFields("id", "displayName", "picture"),
            ).await()
            Ok(Unit)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            BadServer()
        }
    }

    override suspend fun update(partUser: ModelPartUser): Result<Unit> {
        val partUserMap = mutableMapOf<String, Any?>()

        if (partUser.displayNameProvided) {
            partUserMap["displayName"] = partUser.displayName
        }

        val doc = store.collection("users").document(partUser.identifier)
        val bytes = partUser.imageBase64?.let { Base64.decodeBase64(it.encoded) }

        if (partUser.imageBase64Provided && bytes != null) {
            val id = UUID.randomUUID().toString()
            val fileName = "$id.jpg"
            val blob = storage.bucket().create(
                fileName,
                bytes.inputStream(),
                ContentType.Image.JPEG.contentType,
            )
            // TODO: Find alternative to timeout
            val url = blob.signUrl(365, TimeUnit.DAYS)
            partUserMap["picture"] = url.toString()
        } else if (partUser.imageBase64Provided && bytes == null) {
            partUserMap["picture"] = null
        }

        return try {
            if (partUserMap.isNotEmpty()) {
                doc.update(partUserMap).await()
            }
            Ok(Unit)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            BadServer()
        }
    }

    override suspend fun updateLastSeenRecipe(user: User, lastSeenRecipe: Long): Result<Unit> {
        val userDoc = store.collection("users").document(user.id.uid)
        return try {
            userDoc.update("lastSeenRecipe", lastSeenRecipe).await()
            Ok(Unit)
        } catch (throwable: Throwable) {
            BadServer()
        }
    }

    override suspend fun read(
        user: User,
    ): Result<ModelUser> = coroutineScope {
        val firestoreUser = async {
            store.collection("users").document(user.id.uid).get().await()
                .toObject(FirestoreUser::class.java)
        }

        try {
            val result = firestoreUser.await()?.toModelUser()
                ?: return@coroutineScope NotFound()
            Ok(result)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            BadServer()
        }
    }

    override suspend fun register(
        user: User,
        token: ModelNotificationToken,
    ): Result<Unit> {
        // We consider that users will only receive notifications on a single device at a time. This
        // could be handled differently, with lifetimes associated with each token, would each user
        // have a set of phones at their disposals.
        return try {
            store.collection("users")
                .document(user.id.uid)
                .set(mapOf("notifications" to token.value), SetOptions.merge())
                .await()

            // Subscribe to the shared topics.
            messaging.subscribeToTopicAsync(
                listOf(token.value),
                Notifications.TopicStack
            ).await().successCount

            Ok(Unit)
        } catch (throwable: Throwable) {
            BadServer()
        }
    }
}
