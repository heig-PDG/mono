package tupperdate.android.data.features.picker

import android.net.Uri

interface ImagePicker {

    /**
     * A [Handle] lets you access an image, through its [Uri]. If the image is not available
     * anymore, the [Uri] might point to an empty location.
     */
    data class Handle(val uri: Uri)

    /**
     * Picks a new picture, returning the [Handle] to access its data. If the action was not
     * successful, a null value will be returned instead.
     */
    suspend fun pick(): Handle?
}
