package tupperdate.android.data.legacy.api

interface Api {
    val authentication: AuthenticationApi
    val users: UserApi
    val recipe: RecipeApi
    val images: ImagePickerApi
}
