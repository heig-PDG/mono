package tupperdate.api

import androidx.appcompat.app.AppCompatActivity

class MockApi(activity: AppCompatActivity) : Api {
    override val authentication = MockAuthenticationApi
    override val users = MockUserApi
    override val recipe = MockRecipeApi
    override val images = ActualImagePickerApi(activity)
}
