package iuliia.soundrecorder.record

import android.media.MediaRecorder
import java.io.IOException


class RecordModel : RecordContract.Model {
    private var recorder: MediaRecorder? = null

    @Throws(IOException::class)
    override fun startRecording(filePath: String, samplingRate: Int) {
        if (recorder == null) {
            recorder = MediaRecorder()
        }
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioSamplingRate(samplingRate)
            setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
            setAudioEncodingBitRate(samplingRate * BIT_DEPTH)
            prepare()
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

    companion object {
        private const val BIT_DEPTH = 8
    }
}