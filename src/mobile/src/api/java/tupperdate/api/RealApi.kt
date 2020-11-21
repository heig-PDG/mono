package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RealApi(owner: AppCompatActivity) : Api {
    override val authentication = RealAuthenticationApi(owner, FirebaseAuth.getInstance())
    override val recipe = RealRecipeApi
}
