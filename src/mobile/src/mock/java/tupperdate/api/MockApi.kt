package tupperdate.api

object MockApi : Api {
    override val authentication = MockAuthenticationApi
    override val recipe = MockRecipeApi
}