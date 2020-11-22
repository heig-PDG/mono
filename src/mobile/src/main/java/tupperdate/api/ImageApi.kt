package tupperdate.api

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

interface ImageApi {
    fun launch()
    val current: Flow<Uri?>
}

class ActualImageApi(activity: AppCompatActivity) : ImageApi {
    private val authority = "me.tupperdate.provider"

    private val path = File(activity.filesDir, "images").apply { mkdirs() }
    private val file = File(path, "capture.jpg")
    private val uri = FileProvider.getUriForFile(
        activity.applicationContext,
        authority,
        file
    )

    override val current: MutableStateFlow<Uri?>
        get() = MutableStateFlow(null)

    private val registration = activity.registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            current.value = uri
        }
    }

    override fun launch() {
        current.value = null
        registration.launch(uri)
    }
}