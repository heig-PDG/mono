package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RealApi(owner: AppCompatActivity) : Api {
    override val authentication: RealAuthenticationApi
    override val recipe: RealRecipeApi

    init {
        authentication = RealAuthenticationApi(owner, FirebaseAuth.getInstance())
        recipe = RealRecipeApi(authentication)
    }
}
