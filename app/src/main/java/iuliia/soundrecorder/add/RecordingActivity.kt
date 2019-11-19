package iuliia.soundrecorder.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import iuliia.soundrecorder.R
import kotlinx.android.synthetic.main.activity_recording.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordingActivity : AppCompatActivity(), RecordContract.View {

    private lateinit var presenter: RecordContract.Presenter

    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        presenter = RecordPresenter()
        presenter.attachView(this)
        startRecordingButton.setOnClickListener {
            onRecord()
        }
        stopRecordingButton.setOnClickListener {
            presenter.onStopRecording()
        }

    }

    private fun onRecord() {
        if (ContextCompat.checkSelfPermission(this, permissions[0])
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        } else {
            presenter.onStartRecording()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionToRecordAccepted = (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        if (permissionToRecordAccepted) {
            presenter.onStartRecording()
        }

    }

    override fun updateUi(startRecording: Boolean) {
        startRecordingButton.isEnabled = !startRecording
        stopRecordingButton.isEnabled = startRecording
        statusView.text = if (startRecording) getString(R.string.recording) else ""
    }

    override fun getDirectory(): String {
        return externalCacheDir!!.absolutePath
    }

    override fun onStop() {
        super.onStop()
        presenter.onLeaveView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
