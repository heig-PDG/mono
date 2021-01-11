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

@InternalDataApi
@OptIn(KoinApiExtension::class)
class UpdateProfileWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()

    override suspend fun doWork(): Result {
        val uid = inputData.getString(KeyUid) ?: return Result.failure()
        val name = inputData.getString(KeyName) ?: return Result.failure()
        val picture = inputData.getString(KeyPicture)

        return try {
            client.put<Unit>("/users/${uid}") {
                body = MyUserDTO(
                    displayName = name,
                    imageBase64 = picture?.toUri()
                        ?.readFileAndCompressAsBase64(applicationContext.contentResolver),
                )
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

        operator fun invoke(
            uid: String,
            name: String,
            picture: String?,
        ): androidx.work.Data {
            return workDataOf(
                KeyUid to uid,
                KeyName to name,
                KeyPicture to picture,
            )
        }
    }
}
