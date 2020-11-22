package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RealApi(activity: AppCompatActivity) : Api {
    override val authentication = RealAuthenticationApi(activity, FirebaseAuth.getInstance())
    override val recipe = RealRecipeApi(authentication)
    override val images = ActualImagePickerApi(activity)
}
