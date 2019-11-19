package iuliia.soundrecorder.add

import iuliia.soundrecorder.mvp.BasePresenter
import iuliia.soundrecorder.mvp.BaseView

interface RecordContract {
    interface Presenter : BasePresenter<View> {
        fun onStartRecording()
        fun onStopRecording()
        fun onLeaveView()
    }
    interface View: BaseView {
        fun updateUi(startRecording: Boolean)
        fun getDirectory(): String
    }

    interface Model {
        fun startRecording(fileName: String)
        fun stopRecording()
        fun release()
    }
}