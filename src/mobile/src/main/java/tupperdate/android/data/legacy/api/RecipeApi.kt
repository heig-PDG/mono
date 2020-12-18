package tupperdate.android.data.legacy.api

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.Flow
import tupperdate.android.data.legacy.ObsoleteTupperdateApi

@ObsoleteTupperdateApi
interface RecipeApi {

    @Parcelize
    data class Recipe(
        val title: String,
        val description: String,
        val pictureUrl: String,
    ): Parcelable

    suspend fun create(
        title: String,
        description: String,
        vegetarian: Boolean,
        warm: Boolean,
        hasAllergens: Boolean,
        imageUri: Uri?,
    )
}
