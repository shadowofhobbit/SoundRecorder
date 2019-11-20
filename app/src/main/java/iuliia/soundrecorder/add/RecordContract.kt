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
        fun getSamplingRatePreference(): Int
    }

    interface Model {
        fun startRecording(filePath: String, samplingRate: Int)
        fun stopRecording()
        fun release()
    }
}