package iuliia.soundrecorder.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import iuliia.soundrecorder.record.RecordActivity
import iuliia.soundrecorder.record.RecordModule
import iuliia.soundrecorder.recordingslist.RecordingsListActivity
import iuliia.soundrecorder.recordingslist.RecordingsListModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RecordModule::class, RecordingsListModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: RecordActivity)

    fun inject(activity: RecordingsListActivity)
}