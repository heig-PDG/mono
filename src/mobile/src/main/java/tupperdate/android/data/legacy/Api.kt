package tupperdate.android.data.legacy

import androidx.appcompat.app.AppCompatActivity
import tupperdate.android.data.legacy.api.Api

@ObsoleteTupperdateApi
fun AppCompatActivity.api(): Api = RealApi(this)
