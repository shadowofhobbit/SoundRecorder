package iuliia.soundrecorder.record

import iuliia.soundrecorder.mvp.BasePresenter
import iuliia.soundrecorder.mvp.BaseView
import java.io.IOException

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
        fun displayRecordingSaved()
    }

    interface Model {
        @Throws(IOException::class)
        fun startRecording(filePath: String, samplingRate: Int)
        fun stopRecording()
        fun release()
    }
}