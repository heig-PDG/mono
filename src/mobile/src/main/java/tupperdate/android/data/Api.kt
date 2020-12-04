package tupperdate.android.data

import androidx.appcompat.app.AppCompatActivity
import tupperdate.android.data.api.Api

fun AppCompatActivity.api(): Api = RealApi(this)
