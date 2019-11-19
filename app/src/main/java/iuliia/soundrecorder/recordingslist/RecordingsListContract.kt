package iuliia.soundrecorder.recordingslist

import iuliia.soundrecorder.mvp.BasePresenter
import iuliia.soundrecorder.mvp.BaseView

interface RecordingsListContract {
    interface Presenter: BasePresenter<View> {
        fun onStartPlaying(path: String)
        fun onViewReady()
    }

    interface View: BaseView {
        fun updateRecordings()
    }

    interface Model {
        fun startPlaying(path: String)

        fun stopPlaying()
    }
}