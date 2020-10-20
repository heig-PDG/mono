package tupperdate.api

import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.api(): Api = RealApi(this)