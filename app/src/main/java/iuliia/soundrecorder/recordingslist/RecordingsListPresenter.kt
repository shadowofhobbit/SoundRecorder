package iuliia.soundrecorder.recordingslist

class RecordingsListPresenter(private val model: RecordingsListContract.Model) : RecordingsListContract.Presenter {
    private var view: RecordingsListContract.View? = null

    override fun attachView(view: RecordingsListContract.View) {
        this.view = view
    }

    override fun onViewReady() {
        view?.updateRecordings()
    }

    override fun onStartPlaying(path: String) {
        model.startPlaying(path)
    }

    override fun detachView() {
        this.view = null
    }
}