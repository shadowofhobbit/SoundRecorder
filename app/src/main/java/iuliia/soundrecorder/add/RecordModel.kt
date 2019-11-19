package iuliia.soundrecorder.add

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

private const val LOG_TAG = "SoundRecorder"

class RecordModel : RecordContract.Model {
    private var recorder: MediaRecorder? = null

    override fun startRecording(fileName: String) {
        if (recorder == null) {
            recorder = MediaRecorder()
        }
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    override fun release() {
        recorder?.release()
        recorder = null
    }

    override fun stopRecording() {
        recorder?.stop()
        release()
    }
}