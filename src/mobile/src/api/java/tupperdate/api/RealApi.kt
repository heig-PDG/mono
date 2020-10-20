package tupperdate.api

import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth

class RealApi(owner: LifecycleOwner) : Api {
    override val authentication = RealAuthenticationApi(FirebaseAuth.getInstance())
    override val recipe = RealRecipeApi
}