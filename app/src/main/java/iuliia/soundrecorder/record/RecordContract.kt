package iuliia.soundrecorder.record

import iuliia.soundrecorder.mvp.BasePresenter
import iuliia.soundrecorder.mvp.BaseView
import java.io.IOException

interface RecordContract {
    interface Presenter : BasePresenter<View> {
        fun onStartRecording()
        fun onStopRecording()
        fun onLeaveView()
        fun onBluetoothFailed()
        fun onUseBluetooth()
    }

    interface View: BaseView {
        fun updateUi(startRecording: Boolean)
        fun getDirectory(): String
        fun getSamplingRatePreference(): Int
        fun displayRecordingSaved()
        fun getSourcePreference(): Source
        fun displayBluetoothError()
        fun stopReceivingBluetoothEvents()
    }

    interface Model {
        @Throws(IOException::class)
        fun startRecording(filePath: String, samplingRate: Int)
        fun stopRecording()
        fun release()
        fun prepareToUseBluetooth()
    }
}