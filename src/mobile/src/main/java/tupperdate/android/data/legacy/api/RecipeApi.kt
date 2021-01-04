package tupperdate.android.data.legacy.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tupperdate.android.data.legacy.ObsoleteTupperdateApi

@ObsoleteTupperdateApi
interface RecipeApi {

    @Parcelize
    data class Recipe(
        val title: String,
        val description: String,
        val pictureUrl: String,
    ): Parcelable

    fun like(recipe: Recipe)
    fun dislike(recipe: Recipe)
}
