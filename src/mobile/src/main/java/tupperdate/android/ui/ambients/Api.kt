package tupperdate.android.ui.ambients

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.PhoneRegistrationApi
import tupperdate.android.data.features.picker.ImagePicker

/**
 * An ambient that provides a [PhoneRegistrationApi].
 */
val AmbientPhoneRegistration: ProvidableAmbient<PhoneRegistrationApi> =
    ambientOf { error("not provided") }

/**
 * An ambient that provides an [ImagePicker].
 */
val AmbientImagePicker: ProvidableAmbient<ImagePicker> =
    ambientOf { error("not provided") }

/**
 * An ambient that provides the current [AuthenticationStatus]. If the status could not be loaded,
 * a null value will be returned.
 */
val AmbientProfile: ProvidableAmbient<AuthenticationStatus?> =
    ambientOf { null }
