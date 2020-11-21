package tupperdate.android.utils

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher

data class ActivityRegistration(
    val recipePicture: Registration<Void, Bitmap>,
)

data class Registration<I, O>(
    val launcher: ActivityResultLauncher<I>,
    val data: RegistrationData<O>
)

data class RegistrationData<O> (
    var value: O? = null
)
