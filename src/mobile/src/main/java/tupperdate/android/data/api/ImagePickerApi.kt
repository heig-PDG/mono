package tupperdate.android.data.api

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.apache.commons.codec.binary.Base64
import java.io.File

enum class ImageType {
    Profile,
    Recipe;
}

interface ImagePickerApi {
    fun pick(imageType: ImageType)
    val currentRecipe: Flow<Uri?>
    val currentProfile: Flow<Uri?>
}

class ActualImagePickerApi(private val activity: AppCompatActivity) : ImagePickerApi {
    private val authority = "me.tupperdate.provider"
    private val directory = File(activity.filesDir, "images").apply { mkdirs() }

    private val recipe = Type("recipe.jpg")
    private val profile = Type("profile.jpg")

    override val currentRecipe: Flow<Uri?>
        get() = recipe.flow

    override val currentProfile: Flow<Uri?>
        get() = profile.flow

    override fun pick(imageType: ImageType) {
        val type = imageType.toType()
        type.flow.value = null
        type.registration.launch(type.uri)
    }

    /**
     * This class is used to represent different fields that we need for each type of
     * images that we support
     */
    inner class Type(
        fileName: String,
    ) {
        internal var uri: Uri = FileProvider.getUriForFile(
            activity.applicationContext,
            authority,
            File(directory, fileName),
        )
        internal var flow: MutableStateFlow<Uri?> = MutableStateFlow(null)
        internal var registration: ActivityResultLauncher<Uri?>

        init {
            registration = activity.registerForActivityResult(
                ActivityResultContracts.TakePicture()
            ) { success ->
                if (success) {
                    flow.value = uri
                }
            }
        }
    }

    /**
     * Extension method to map an [ImageType] to an internally used [Type]
     */
    private fun ImageType.toType(): Type {
        return when (this) {
            ImageType.Profile -> profile
            ImageType.Recipe -> recipe
        }
    }

}

/**
 * Extension method used to help us translate images to base64 encoded string, to transmit them
 * on the network
 */
fun Uri.readFileAsBase64(contentResolver: ContentResolver): String? =
    contentResolver.openInputStream(this)
        ?.buffered()
        ?.readBytes()
        ?.let(Base64::encodeBase64String)
