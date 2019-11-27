package iuliia.soundrecorder.recordingslist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.android.material.snackbar.Snackbar
import iuliia.soundrecorder.App
import iuliia.soundrecorder.R
import iuliia.soundrecorder.settings.SettingsActivity
import iuliia.soundrecorder.record.RecordActivity
import iuliia.soundrecorder.getDirectory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class RecordingsListActivity : AppCompatActivity(), RecordingsListContract.View, OnListInteractionListener {
    @Inject
    lateinit var presenter: RecordingsListContract.Presenter
    private lateinit var adapter: RecordingAdapter
    private lateinit var adapterDataObserver: AdapterDataObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recordingsView.layoutManager = LinearLayoutManager(this)
        adapter = RecordingAdapter(arrayListOf(), this)
        recordingsView.adapter = adapter
        adapterDataObserver = object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                showOrHideRecyclerView()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                showOrHideRecyclerView()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                showOrHideRecyclerView()
            }
        }
        presenter.attachView(this)

        fab.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewReady()
    }

    override fun onResume() {
        super.onResume()
        adapter.registerAdapterDataObserver(adapterDataObserver)
        showOrHideRecyclerView()
    }

    private fun showOrHideRecyclerView() {
        if (adapter.itemCount == 0) {
            recordingsView.visibility = View.INVISIBLE
            emptyView.visibility = View.VISIBLE
        } else {
            recordingsView.visibility = View.VISIBLE
            emptyView.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        adapter.unregisterAdapterDataObserver(adapterDataObserver)
    }

    override fun updateRecordings() {
        val recordings = getDirectory(this)
            .list()
            ?.map { title -> Recording(title) }
            ?: arrayListOf()
        adapter.setData(recordings)
    }

    override fun displayError() {
        Snackbar.make(recordingsView, getString(R.string.error_playing), Snackbar.LENGTH_SHORT)
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onListInteraction(item: Recording) {
        presenter.onStartPlaying("${getDirectory(this).absolutePath}/${item.title}")
    }
}
