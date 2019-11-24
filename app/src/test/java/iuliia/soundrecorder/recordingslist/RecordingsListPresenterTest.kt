package iuliia.soundrecorder.recordingslist

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

class RecordingsListPresenterTest {

    @Mock
    private lateinit var model: RecordingsListContract.Model

    @Mock
    private lateinit var view: RecordingsListContract.View

    private lateinit var presenter: RecordingsListPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = RecordingsListPresenter(model)
        presenter.attachView(view)
    }

    @Test
    fun onViewReady() {
        presenter.onViewReady()
        verify(view).updateRecordings()
    }

    @Test
    fun onStartPlaying() {
        val path = "/it/is/a/path"
        presenter.onStartPlaying(path)
        verify(model).startPlaying(path)
        verifyZeroInteractions(view)
    }

    @Test
    fun onStartPlayingError() {
        `when`(model.startPlaying(anyString())).thenThrow(IOException::class.java)
        val path = "/it/is/a/path"
        presenter.onStartPlaying(path)
        verify(model).startPlaying(path)
        verify(view).displayError()
    }
}