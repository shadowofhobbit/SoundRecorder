package iuliia.soundrecorder.record

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import iuliia.soundrecorder.R
import iuliia.soundrecorder.getDirectory
import iuliia.soundrecorder.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_recording.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordActivity : AppCompatActivity(), RecordContract.View {

    private lateinit var presenter: RecordContract.Presenter

    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        presenter = RecordPresenter(RecordModel())
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
        return getDirectory(this).absolutePath
    }

    override fun getSamplingRatePreference(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val samplingRate: String = preferences.getString(
            "samplingRate",
            resources.getString(R.string.default_sampling_rate)
        ) as String
        return Integer.valueOf(samplingRate)
    }

    override fun displayRecordingSaved() {
        Snackbar.make(recordingLayout, R.string.saved, Snackbar.LENGTH_SHORT).show()
    }

    override fun displayError() {
        Snackbar.make(recordingLayout, getString(R.string.error_recording), Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
