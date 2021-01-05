package tupperdate.android

import android.app.Application

class Tupperdate : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }
}
