package iuliia.soundrecorder.recordingslist

import java.io.IOException
import javax.inject.Inject

class RecordingsListPresenter @Inject constructor(private val model: RecordingsListContract.Model) : RecordingsListContract.Presenter {
    private var view: RecordingsListContract.View? = null

    override fun attachView(view: RecordingsListContract.View) {
        this.view = view
    }

    override fun onViewReady() {
        view?.updateRecordings()
    }

    override fun onStartPlaying(path: String) {
        try {
            model.startPlaying(path)
        } catch (e: IOException) {
            view?.displayError()
        }
    }

    override fun detachView() {
        this.view = null
    }
}