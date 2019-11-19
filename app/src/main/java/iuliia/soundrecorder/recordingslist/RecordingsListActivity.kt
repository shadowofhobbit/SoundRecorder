package iuliia.soundrecorder.recordingslist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import iuliia.soundrecorder.R
import iuliia.soundrecorder.add.RecordingActivity
import iuliia.soundrecorder.getDirectory
import kotlinx.android.synthetic.main.activity_main.*

class RecordingsListActivity : AppCompatActivity(), RecordingsListContract.View, OnListInteractionListener {
    private lateinit var presenter: RecordingsListContract.Presenter
    private lateinit var adapter: RecordingAdapter
    private lateinit var adapterDataObserver: AdapterDataObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recordingsView.layoutManager = LinearLayoutManager(this)
        presenter = RecordingsListPresenter(RecordingsListModel())
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
            val intent = Intent(this, RecordingActivity::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
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