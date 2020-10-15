package tupperdate.api

interface Api {
    val authentication: AuthenticationApi
    val recipe: RecipeApi
}