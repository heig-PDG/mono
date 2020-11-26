package tupperdate.api

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

enum class ImageType {
    Profile,
    Recipe
}

interface ImagePickerApi {
    fun pick(type: ImageType)
    val currentRecipe: Flow<Uri?>
    val currentProfile: Flow<Uri?>
}

class ActualImagePickerApi(private val activity: AppCompatActivity) : ImagePickerApi {
    private val authority = "me.tupperdate.provider"

    private val path = File(activity.filesDir, "images").apply { mkdirs() }
    private val file = File(path, "capture.jpg")
    private val uri = FileProvider.getUriForFile(
        activity.applicationContext,
        authority,
        file
    )

    private val uri2 = FileProvider.getUriForFile(
        activity.applicationContext,
        authority,
        File(path, "profile.jpg")
    )

    private val uriRecipeFlow = MutableStateFlow<Uri?>(null)
    private val uriProfileFlow = MutableStateFlow<Uri?>(null)

    override val currentRecipe: Flow<Uri?>
        get() = uriRecipeFlow

    override val currentProfile: Flow<Uri?>
        get() = uriProfileFlow

    private fun register(flow: MutableStateFlow<Uri?>, uri: Uri) : ActivityResultLauncher<Uri?> {
        return activity.registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                flow.value = uri
            }
        }
    }

    override fun pick(type: ImageType) {
        when (type) {
            ImageType.Profile -> {
                uriProfileFlow.value = null
                register(uriProfileFlow, uri2).launch(uri2)
            }
            ImageType.Recipe -> {
                uriRecipeFlow.value = null
                register(uriRecipeFlow, uri).launch(uri)
            }
        }
    }
}
