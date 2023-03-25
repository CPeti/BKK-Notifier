package hu.bme.aut.bkknotifier.feature.stop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.ActivityStopBinding
import hu.bme.aut.bkknotifier.feature.details.DetailsActivity
import hu.bme.aut.bkknotifier.feature.stop.data.Stop
import hu.bme.aut.bkknotifier.feature.stop.data.StopDatabase
import kotlin.concurrent.thread


class StopActivity : AppCompatActivity(), StopAdapter.OnStopSelectedListener, AddStopDialogFragment.AddStopDialogListener {
    private lateinit var binding: ActivityStopBinding
    private lateinit var adapter: StopAdapter
    private lateinit var database: StopDatabase
    private var startup = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = StopDatabase.getDatabase(applicationContext)

        initFab()
        initRecyclerView()


    }

    private fun initFab(){
        binding.fab.setOnClickListener{
            AddStopDialogFragment().show(supportFragmentManager, AddStopDialogFragment::class.java.simpleName)
        }
    }

    private fun loadStops(){
        thread {
            val stops = database.stopDao().getAll()
            runOnUiThread{
                adapter.update(stops)
                if(startup){
                    adapter.initService(applicationContext)
                    startup = false
                }
            }
        }
    }

    private fun initRecyclerView(){
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.mainRecyclerView.context,
            (binding.mainRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        ContextCompat.getDrawable(this, R.drawable.divider)?.let { dividerItemDecoration.setDrawable(it) }
        binding.mainRecyclerView.addItemDecoration(dividerItemDecoration)
        adapter = StopAdapter(this)
        binding.mainRecyclerView.adapter = adapter
        loadStops()
    }

    override fun onStopSelected(stop: Stop?) {
        val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@StopActivity, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_STOP, stop)
        startActivity(showDetailsIntent)
    }

    override fun onStopRemoved(stop: Stop){
        thread {
            adapter.removeStop(stop)
            database.stopDao().delete(stop)
            loadStops()
        }

    }

    override fun onSwitched(stop: Stop, state: Boolean) {
        thread {
            stop.alerts = database.stopDao().getStop(stop.id).alerts
            stop.toggle = state
            database.stopDao().update(stop)
            runOnUiThread{
                adapter.handleSwitchAction(stop, state)
            }
        }
    }

    override fun onStopCreated(stop: Stop) {
        if(adapter.contains(stop)){
            Toast.makeText(this, getString(R.string.duplicate_id), Toast.LENGTH_LONG).show()
            return
        }
        thread {
            database.stopDao().insert(stop)
            runOnUiThread {
                adapter.addStop(stop)
            }
        }
    }
}