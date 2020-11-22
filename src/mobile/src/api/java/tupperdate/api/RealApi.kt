package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RealApi(activity: AppCompatActivity) : Api {
    override val authentication: RealAuthenticationApi
    override val recipe: RealRecipeApi
    override val images: ImagePickerApi

    init {
        authentication = RealAuthenticationApi(activity, FirebaseAuth.getInstance())
        recipe = RealRecipeApi(authentication)
        images = ActualImagePickerApi(activity)
    }
}
