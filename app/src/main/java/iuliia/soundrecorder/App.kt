package iuliia.soundrecorder

import android.app.Application
import iuliia.soundrecorder.di.AppComponent
import iuliia.soundrecorder.di.DaggerAppComponent

class App : Application() {
    val appComponent : AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}