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
        fun onViewReady()
    }

    interface View: BaseView {
        fun updateUi(startRecording: Boolean)
        val directory: String
        fun getSamplingRatePreference(): Int
        fun displayRecordingSaved()
        fun getSourcePreference(): Source
        fun displayBluetoothError()
        fun stopReceivingBluetoothEvents()
        fun startSoundVisualizerUpdates(getMaxAmplitude: () -> Int)
        fun stopSoundVisualizerUpdates()
    }

    interface Model {
        @Throws(IOException::class)
        fun startRecording(filePath: String, samplingRate: Int)
        fun stopRecording()
        fun release()
        fun prepareToUseBluetooth()
        val maxAmplitude: Int
    }
}