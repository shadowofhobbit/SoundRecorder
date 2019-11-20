package iuliia.soundrecorder.add

import java.text.SimpleDateFormat
import java.util.*


class RecordPresenter : RecordContract.Presenter {
    private val model: RecordContract.Model = RecordModel()

    private var view: RecordContract.View? = null

    override fun attachView(view: RecordContract.View) {
        this.view = view
    }

    override fun onStartRecording() {
        val now = Date()
        val formattedDate = SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.ENGLISH).format(now)
        val filePath = "${view?.getDirectory()}/${formattedDate}.3gp"
        view?.updateUi(true)
        model.startRecording(filePath, view!!.getSamplingRatePreference())
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