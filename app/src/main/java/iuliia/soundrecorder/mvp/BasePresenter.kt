package iuliia.soundrecorder.mvp


interface BasePresenter<V : BaseView> {
    fun attachView(view: V)
    fun detachView()
}


