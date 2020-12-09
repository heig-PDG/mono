package tupperdate.android.data.legacy.api

import tupperdate.android.data.legacy.ObsoleteTupperdateApi

@ObsoleteTupperdateApi
interface Api {
    val authentication: AuthenticationApi
    val users: UserApi
    val recipe: RecipeApi
    val images: ImagePickerApi
}
