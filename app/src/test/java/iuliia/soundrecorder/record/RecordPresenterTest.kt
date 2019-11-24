package iuliia.soundrecorder.record

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

class RecordPresenterTest {
    @Mock
    private lateinit var view: RecordContract.View

    @Mock
    private lateinit var model: RecordContract.Model

    private lateinit var presenter: RecordPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = RecordPresenter(model)
        presenter.attachView(view)
    }

    @Test
    fun onStartRecordingError() {
        `when`(model.startRecording(anyString(), anyInt())).thenThrow(IOException::class.java)
        presenter.onStartRecording()
        verify(view).getDirectory()
        verify(view, times(1)).updateUi(true)
        verify(view).getSamplingRatePreference()
        verify(model).startRecording(anyString(), anyInt())
        verify(view).displayError()
        verify(view).updateUi(false)
        verify(model).release()
        verify(view).stopReceivingBluetoothEvents()
    }

    @Test
    fun onStartRecordingNoErrors() {
        presenter.onStartRecording()
        verify(view).getDirectory()
        verify(view).updateUi(true)
        verify(view).getSamplingRatePreference()
        verify(model).startRecording(anyString(), anyInt())
        verifyNoMoreInteractions(model)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onStopRecording() {
        presenter.onStopRecording()
        verify(view).updateUi(false)
        verify(model).stopRecording()
        verify(view).displayRecordingSaved()
        verify(view).stopReceivingBluetoothEvents()
    }

    @Test
    fun onLeaveView() {
        presenter.onLeaveView()
        verify(view).stopReceivingBluetoothEvents()
        verify(model).release()
    }
}