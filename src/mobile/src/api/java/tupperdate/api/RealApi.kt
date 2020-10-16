package tupperdate.api

object RealApi : Api {
    override val authentication = RealAuthenticationApi
    override val recipe = RealRecipeApi
}