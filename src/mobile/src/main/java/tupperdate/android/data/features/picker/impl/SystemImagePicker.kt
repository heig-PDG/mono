package tupperdate.android.data.features.picker.impl

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.core.content.FileProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.RequiresParameterInjection
import tupperdate.android.data.features.picker.ImagePicker
import tupperdate.android.data.features.picker.work.RemoveImageWorker
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * An implementation of an [ImagePicker] that stores images on the disk for a certain timeout,
 * before they get cleaned up by the OS.
 */
@RequiresParameterInjection
@OptIn(InternalDataApi::class)
class SystemImagePicker(
    private val activity: ComponentActivity,
) : ImagePicker {

    private val pending = MutableStateFlow<Boolean?>(null)

    private val picker = activity.registerForActivityResult(TakePicture()) { ok ->
        pending.value = ok
    }

    override suspend fun pick(): ImagePicker.Handle? = withContext(Dispatchers.Main) {
        val uri = randomUri()

        // Pick a new image.
        pending.value = null
        picker.launch(uri)

        // Eventually delete the image.
        WorkManager.getInstance(activity)
            .enqueue(
                OneTimeWorkRequestBuilder<RemoveImageWorker>()
                    .setInputData(RemoveImageWorker.forUri(uri))
                    .setInitialDelay(ImagesTimeoutHours, TimeUnit.HOURS)
                    .build()
            )

        // Await the result before sending back the uri.
        val ok = pending.filterNotNull().firstOrNull() ?: false

        // Return the request result.
        if (!ok) return@withContext null
        return@withContext ImagePicker.Handle(uri)
    }

    /**
     * Generates a random [Uri] that can be used to save a new image file. It is guaranteed to be
     * unique.
     */
    private fun randomUri(): Uri {
        val directory = File(activity.filesDir, ImagesDir).apply { mkdirs() }
        val file = File(directory, "${UUID.randomUUID()}.jpg")
        return FileProvider.getUriForFile(activity, ImagesProviderAuthority, file)
    }
}

private const val ImagesDir = "images"
private const val ImagesProviderAuthority = "me.tupperdate.provider"
private const val ImagesTimeoutHours = 24L
