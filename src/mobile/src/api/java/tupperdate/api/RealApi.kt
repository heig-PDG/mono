package tupperdate.api

import androidx.appcompat.app.AppCompatActivity

class RealApi(activity: AppCompatActivity) : Api {
    override val authentication = RealAuthenticationApi(activity)
    override val recipe = RealRecipeApi
}