package tupperdate.android.ui.ambients

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import tupperdate.android.data.features.auth.AuthenticationStatus
import tupperdate.android.data.features.auth.PhoneRegistrationApi
import tupperdate.android.data.features.picker.ImagePicker

val AmbientPhoneRegistration: ProvidableAmbient<PhoneRegistrationApi> =
    ambientOf { error("not provided") }

val AmbientImagePicker: ProvidableAmbient<ImagePicker> =
    ambientOf { error("not provided") }

val AmbientProfile: ProvidableAmbient<AuthenticationStatus?> =
    ambientOf { error("not provided") }
