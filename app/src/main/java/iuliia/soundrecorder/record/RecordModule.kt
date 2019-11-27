package iuliia.soundrecorder.record

import dagger.Binds
import dagger.Module

@Module
abstract class RecordModule {
    @Binds
    abstract fun providePresenter(presenter: RecordPresenter): RecordContract.Presenter

    @Binds
    abstract fun provideModel(model: RecordModel): RecordContract.Model
}