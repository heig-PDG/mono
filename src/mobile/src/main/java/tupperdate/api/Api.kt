package tupperdate.api

interface Api {
    val authentication: AuthenticationApi
    val users: UserApi
    val recipe: RecipeApi
    val images: ImagePickerApi
}
