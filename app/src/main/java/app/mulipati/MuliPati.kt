package app.mulipati

import android.app.Application
import timber.log.Timber

class MuliPati: Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

}