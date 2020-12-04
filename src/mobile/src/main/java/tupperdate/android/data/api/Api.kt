package tupperdate.android.data.api

interface Api {
    val authentication: AuthenticationApi
    val users: UserApi
    val recipe: RecipeApi
    val images: ImagePickerApi
}
