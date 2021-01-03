package tupperdate.android.data.features.picker

import org.koin.dsl.module
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.picker.impl.ImagePickerImpl

@OptIn(InternalDataApi::class)
val KoinPickerModule = module {
    // Uses registerForActivityResult, so createdAtStart MUST be true.
    single<ImagePicker>(createdAtStart = true) { ImagePickerImpl(get()) }
}
