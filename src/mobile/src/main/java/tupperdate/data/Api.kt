package tupperdate.data

import androidx.appcompat.app.AppCompatActivity
import tupperdate.api.Api

fun AppCompatActivity.api(): Api = RealApi(this)
