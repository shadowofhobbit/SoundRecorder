package iuliia.soundrecorder.recordingslist

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

private const val LOG_TAG = "SoundRecorder"

class RecordingsListModel : RecordingsListContract.Model {
    private var player: MediaPlayer? = null

    override fun startPlaying(path: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource(path)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    override fun stopPlaying() {
        player?.release()
        player = null
    }
}