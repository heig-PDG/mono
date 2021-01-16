package tupperdate.android.data.features.auth.work

import android.content.Context
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.readFileAndCompressAsBase64
import tupperdate.common.dto.MyUserDTO
import tupperdate.common.dto.MyUserPartDTO
import utils.OptionalProperty.NotProvided
import utils.OptionalProperty.Provided

@InternalDataApi
@OptIn(KoinApiExtension::class)
class UpdateProfileWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()

    override suspend fun doWork(): Result {
        val uid = inputData.getString(KeyUid) ?: return Result.failure()
        val name = inputData.getString(KeyName) ?: ""
        val picture = inputData.getString(KeyPicture)

        val updateName = inputData.getBoolean(KeyUpdateName, true)
        val updatePicture = inputData.getBoolean(KeyUpdatePicture, true)

        return try {
            val encodedPicture = picture?.toUri()
                ?.readFileAndCompressAsBase64(applicationContext.contentResolver)

            if (updateName && updatePicture) {
                // Technically, we could use our PATCH endpoint for this case as well, but we prefer
                // to keep the semantic of an all-or-nothing change.
                client.put<Unit>("/users/${uid}") {
                    body = MyUserDTO(
                        displayName = name,
                        imageBase64 = encodedPicture,
                    )
                }
            } else {
                client.patch<Unit>("/users/${uid}") {
                    body = MyUserPartDTO(
                        displayName = if (updateName) Provided(name) else NotProvided,
                        imageBase64 = if (updatePicture) Provided(encodedPicture) else NotProvided,
                    )
                }
            }
            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.retry()
        }
    }

    companion object Data {

        private const val KeyUid = "uid"
        private const val KeyName = "name"
        private const val KeyPicture = "picture"
        private const val KeyUpdateName = "updateName"
        private const val KeyUpdatePicture = "updatePicture"

        operator fun invoke(
            uid: String,
            name: String,
            picture: String?,
            updateName: Boolean,
            updatePicture: Boolean,
        ): androidx.work.Data {
            return workDataOf(
                KeyUid to uid,
                KeyName to name,
                KeyPicture to picture,
                KeyUpdateName to updateName,
                KeyUpdatePicture to updatePicture,
            )
        }
    }
}
