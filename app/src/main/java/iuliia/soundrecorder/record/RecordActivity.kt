package iuliia.soundrecorder.record

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.AudioManager.*
import android.os.Bundle
import android.os.Handler
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

    private lateinit var receiver: BroadcastReceiver

    private val handler: Handler = Handler()
    private lateinit var updater: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        presenter = RecordPresenter(RecordModel(audioManager))
        presenter.attachView(this)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val state = intent.getIntExtra(EXTRA_SCO_AUDIO_STATE, SCO_AUDIO_STATE_ERROR)
                val previousState =
                    intent.getIntExtra(EXTRA_SCO_AUDIO_PREVIOUS_STATE, SCO_AUDIO_STATE_ERROR)
                when (state) {
                    SCO_AUDIO_STATE_CONNECTED -> {
                        presenter.onStartRecording()
                    }
                    SCO_AUDIO_STATE_ERROR -> {
                        displayError()
                    }
                    SCO_AUDIO_STATE_DISCONNECTED -> {
                        if (previousState == SCO_AUDIO_STATE_CONNECTING) {
                            presenter.onBluetoothFailed()
                        }
                    }
                }
            }
        }
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
            val sourcePreference = getSourcePreference()
            if (sourcePreference == Source.BLUETOOTH) {
                presenter.onUseBluetooth()
                val intent =
                    registerReceiver(receiver, IntentFilter(ACTION_SCO_AUDIO_STATE_UPDATED))
                val state = intent?.getIntExtra(EXTRA_SCO_AUDIO_STATE, SCO_AUDIO_STATE_ERROR)
                if (state == SCO_AUDIO_STATE_CONNECTED) {
                    presenter.onStartRecording()
                } else if (state == SCO_AUDIO_STATE_ERROR) {
                    displayError()
                }
            } else {
                presenter.onStartRecording()
            }
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
        if (startRecording) {
            visualizerView.clear()
        }
    }

    override fun startSoundVisualizerUpdates(getMaxAmplitude: () -> Int) {
        updater = object : Runnable {
            override fun run() {
                handler.postDelayed(this, 1)
                val maxAmplitude: Int = getMaxAmplitude()
                if (maxAmplitude != 0) {
                    visualizerView.addAmplitude(maxAmplitude)
                }
            }
        }
        handler.post(updater)
    }

    override val directory: String
        get() {
            return getDirectory(this).absolutePath
        }

    override fun stopSoundVisualizerUpdates() {
        handler.removeCallbacks(updater)
    }

    override fun getSamplingRatePreference(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val samplingRate: String = preferences.getString(
            "samplingRate",
            resources.getString(R.string.default_sampling_rate)
        ) as String
        return Integer.valueOf(samplingRate)
    }

    override fun getSourcePreference(): Source {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val source: String = preferences.getString(
            "source",
            resources.getString(R.string.default_source)
        ) as String
        return Source.valueOf(source)
    }

    override fun displayRecordingSaved() {
        Snackbar.make(recordingLayout, R.string.saved, Snackbar.LENGTH_SHORT).show()
    }

    override fun displayError() {
        Snackbar.make(recordingLayout, R.string.error_recording, Snackbar.LENGTH_SHORT).show()
    }

    override fun displayBluetoothError() {
        Snackbar.make(recordingLayout, R.string.error_bluetooth, Snackbar.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        presenter.onViewReady()
    }

    override fun onStop() {
        super.onStop()
        presenter.onLeaveView()
    }

    override fun stopReceivingBluetoothEvents() {
        try {
            unregisterReceiver(receiver)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
