package iuliia.soundrecorder.record

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class RecordPresenter @Inject constructor(private val model: RecordContract.Model) : RecordContract.Presenter {

    private var view: RecordContract.View? = null

    override fun attachView(view: RecordContract.View) {
        this.view = view
    }

    override fun onStartRecording() {
        val now = Date()
        val formattedDate = SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.ENGLISH).format(now)
        val filePath = "${view?.directory}/${formattedDate}.3gp"
        view?.apply {
            updateUi(true)
            try {
                model.startRecording(filePath, getSamplingRatePreference())
            } catch (e: IOException) {
                File(filePath).delete()
                displayError()
                updateUi(false)
                model.release()
                view?.stopReceivingBluetoothEvents()
            }
        }
    }

    override fun onViewReady() {
        view?.startSoundVisualizerUpdates { model.maxAmplitude }
    }

    override fun onStopRecording() {
        view?.updateUi(false)
        model.stopRecording()
        view?.displayRecordingSaved()
        view?.stopReceivingBluetoothEvents()
    }

    override fun onUseBluetooth() {
        model.prepareToUseBluetooth()
    }

    override fun onBluetoothFailed() {
        view?.apply {
            displayBluetoothError()
            stopReceivingBluetoothEvents()
        }
        model.release()
    }

    override fun onLeaveView() {
        view?.stopReceivingBluetoothEvents()
        view?.stopSoundVisualizerUpdates()
        model.release()
    }

    override fun detachView() {
        this.view = null
    }

}