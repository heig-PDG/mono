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
    Profile {
        override fun getFileName(): String {
            return "profile.jpg"
        }
    },
    Recipe {
        override fun getFileName(): String {
            return "recipe.jpg"
        }
    };

    abstract fun getFileName(): String
}

interface ImagePickerApi {
    fun pick(type: ImageType)
    val currentRecipe: Flow<Uri?>
    val currentProfile: Flow<Uri?>
}

class ActualImagePickerApi(private val activity: AppCompatActivity) : ImagePickerApi {
    private val authority = "me.tupperdate.provider"

    private val path = File(activity.filesDir, "images").apply { mkdirs() }

    private val uriRecipeFlow = MutableStateFlow<Uri?>(null)
    private val uriProfileFlow = MutableStateFlow<Uri?>(null)

    private fun uri(type: ImageType) : Uri = FileProvider.getUriForFile(
        activity.applicationContext,
        authority,
        File(path, type.getFileName())
    )

    private fun getFlow(type: ImageType) : MutableStateFlow<Uri?> {
        return when (type) {
            ImageType.Profile -> uriProfileFlow
            ImageType.Recipe -> uriRecipeFlow
        }
    }

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
        val uri = uri(type)
        val flow = getFlow(type)

        flow.value = null
        register(flow, uri).launch(uri)
    }
}
