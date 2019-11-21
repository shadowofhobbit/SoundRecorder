package iuliia.soundrecorder.recordingslist

import android.media.MediaPlayer
import java.io.IOException


class RecordingsListModel : RecordingsListContract.Model {
    private var player: MediaPlayer? = null

    @Throws(IOException::class)
    override fun startPlaying(path: String) {
        player = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
        }
    }

    override fun stopPlaying() {
        player?.release()
        player = null
    }
}