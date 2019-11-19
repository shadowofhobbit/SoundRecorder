package iuliia.soundrecorder.recordingslist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iuliia.soundrecorder.R
import kotlinx.android.synthetic.main.item_recording.view.*

/**
 * [RecyclerView.Adapter] that can display a [Recording] and makes a call to the
 * specified [OnListInteractionListener].
 */
class RecordingAdapter(
    private var values: List<Recording>,
    private val listener: OnListInteractionListener?
) : RecyclerView.Adapter<RecordingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recording, parent, false)
        val holder = ViewHolder(view)
        holder.playButton.setOnClickListener{listener?.onListInteraction(values[holder.adapterPosition])}
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.titleView
        val playButton: ImageButton = view.playButton
    }

    fun setData(recordings: List<Recording>) {
        values = recordings
        notifyDataSetChanged()
    }
}

data class Recording(val title: String)

interface OnListInteractionListener {
    fun onListInteraction(item: Recording)
}
