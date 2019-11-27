package iuliia.soundrecorder.recordingslist

import dagger.Binds
import dagger.Module

@Module
abstract class RecordingsListModule {
    @Binds
    abstract fun providePresenter(presenter: RecordingsListPresenter): RecordingsListContract.Presenter

    @Binds
    abstract fun provideModel(model: RecordingsListModel): RecordingsListContract.Model
}