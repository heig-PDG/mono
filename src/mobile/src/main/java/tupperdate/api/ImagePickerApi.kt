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
    Recipe;
}

interface ImagePickerApi {
    fun pick(type: ImageType)
    val currentRecipe: Flow<Uri?>
    val currentProfile: Flow<Uri?>
}

class ActualImagePickerApi(private val activity: AppCompatActivity) : ImagePickerApi {
    private val authority = "me.tupperdate.provider"
    private val directory = File(activity.filesDir, "images").apply { mkdirs() }

    private val recipe = initType("recipe.jpg")
    private val profile = initType("profile.jpg")

    override val currentRecipe: Flow<Uri?>
        get() = recipe.flow

    override val currentProfile: Flow<Uri?>
        get() = profile.flow

    private fun initType(fileName: String): Type {
        val uri = FileProvider.getUriForFile(
            activity.applicationContext,
            authority,
            File(directory, fileName),
        )
        val flow = MutableStateFlow<Uri?>(null)
        val registration = register(flow, uri)

        return Type(uri, flow, registration)
    }

    private fun register(flow: MutableStateFlow<Uri?>, uri: Uri): ActivityResultLauncher<Uri?> {
        return activity.registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                flow.value = uri
            }
        }
    }

    override fun pick(imageType: ImageType) {
        val type = imageType.toType()
        type.flow.value = null
        type.registration.launch(type.uri)
    }

    data class Type(
        val uri: Uri,
        val flow: MutableStateFlow<Uri?>,
        val registration: ActivityResultLauncher<Uri?>,
    )

    private fun ImageType.toType() : Type {
        return when (this) {
            ImageType.Profile -> profile
            ImageType.Recipe -> recipe
        }
    }

}
