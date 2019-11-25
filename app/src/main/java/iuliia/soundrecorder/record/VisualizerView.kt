package iuliia.soundrecorder.record

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import iuliia.soundrecorder.R

class VisualizerView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private lateinit var lines: FloatArray
    private var currentIndex = 0
    private val linePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
        strokeWidth = 1F
    }
    private var currentWidth = 0
    private var currentHeight = 0

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        currentWidth = width
        currentHeight = height
        lines = FloatArray(currentWidth * 4)
    }

    fun addAmplitude(amplitude: Int) {
        invalidate()
        val scaledHeight =
            amplitude.toFloat() / MAX_AMPLITUDE * (currentHeight - 1)
        var index = currentIndex * 4
        lines[index++] = currentIndex.toFloat() // x0
        lines[index++] = 0F // y0
        lines[index++] = currentIndex.toFloat() // x1
        lines[index] = scaledHeight // y1
        currentIndex = if (++currentIndex >= currentWidth) 0 else currentIndex
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLines(lines, linePaint)
    }

    fun clear() {
        lines = FloatArray(this.currentWidth * 4)
        currentIndex = 0
    }

    companion object {
        private const val MAX_AMPLITUDE = 32767
    }
}