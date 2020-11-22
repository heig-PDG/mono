package tupperdate.api

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

interface ImagePickerApi {
    fun pick()
    val current: Flow<Uri?>
}

class ActualImagePickerApi(activity: AppCompatActivity) : ImagePickerApi {
    private val authority = "me.tupperdate.provider"

    private val path = File(activity.filesDir, "images").apply { mkdirs() }
    private val file = File(path, "capture.jpg")
    private val uri = FileProvider.getUriForFile(
        activity.applicationContext,
        authority,
        file
    )

    private val uriFlow = MutableStateFlow<Uri?>(null)

    override val current: Flow<Uri?>
        get() = uriFlow

    private val registration = activity.registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            uriFlow.value = uri
        }
    }

    override fun pick() {
        uriFlow.value = null
        registration.launch(uri)
    }
}
