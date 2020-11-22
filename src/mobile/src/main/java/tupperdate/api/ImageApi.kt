package tupperdate.api

import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

typealias Image = Bitmap

interface ImageApi {
    fun launch()
    fun read(): Flow<Image>
}

class ActualImageApi(activity: AppCompatActivity) : ImageApi {
    private val image = MutableStateFlow<Image?>(null)
    private val registration = activity.registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) {
        image.value = it
    }

    override fun launch() {
        registration.launch(null)
    }

    override fun read(): Flow<Image> {
        return image.filterNotNull()
    }

}