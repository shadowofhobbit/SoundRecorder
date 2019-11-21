package iuliia.soundrecorder.add

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RecordPresenter(private val model: RecordContract.Model) : RecordContract.Presenter {

    private var view: RecordContract.View? = null

    override fun attachView(view: RecordContract.View) {
        this.view = view
    }

    override fun onStartRecording() {
        val now = Date()
        val formattedDate = SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.ENGLISH).format(now)
        val filePath = "${view?.getDirectory()}/${formattedDate}.3gp"
        view?.apply {
            updateUi(true)
            try {
                model.startRecording(filePath, getSamplingRatePreference())
            } catch (e: IOException) {
                File(filePath).delete()
                displayError()
                updateUi(false)
                model.release()
            }
        }
    }

    override fun onStopRecording() {
        view?.updateUi(false)
        model.stopRecording()
    }

    override fun onLeaveView() {
        model.release()
    }

    override fun detachView() {
        this.view = null
    }
}