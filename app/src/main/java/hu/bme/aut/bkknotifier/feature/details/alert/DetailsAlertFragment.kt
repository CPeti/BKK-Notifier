package hu.bme.aut.bkknotifier.feature.details.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.FragmentDetailsAlertBinding
import hu.bme.aut.bkknotifier.feature.details.DetailsActivity
import hu.bme.aut.bkknotifier.feature.details.StopDataHolder
import hu.bme.aut.bkknotifier.feature.stop.data.Alert
import hu.bme.aut.bkknotifier.feature.stop.data.StopDatabase
import kotlin.concurrent.thread

class DetailsAlertFragment : Fragment(), AddAlertDialogFragment.AddAlertDialogListener, AlertAdapter.OnAlertSelectedListener{

    private lateinit var binding: FragmentDetailsAlertBinding
    private lateinit var adapter: AlertAdapter
    private lateinit var database: StopDatabase
    private var stopDataHolder: StopDataHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopDataHolder = if (activity is StopDataHolder) {
            activity as StopDataHolder?
        } else {
            throw RuntimeException("Activity must implement stopDataHolder interface!")
        }
        database = StopDatabase.getDatabase((activity as DetailsActivity).applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsAlertBinding.inflate(LayoutInflater.from(context))
        initRecyclerView()
        initFab()
        return binding.root
    }

    private fun initRecyclerView(){
        binding.alertRecyclerView.layoutManager = LinearLayoutManager(this.context)
        val dividerItemDecoration = DividerItemDecoration(
            binding.alertRecyclerView.context,
            (binding.alertRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        this.context?.let { it -> ContextCompat.getDrawable(it, R.drawable.divider)?.let { dividerItemDecoration.setDrawable(it) } }
        binding.alertRecyclerView.addItemDecoration(dividerItemDecoration)
        adapter = AlertAdapter(this)
        binding.alertRecyclerView.adapter = adapter
        loadAlerts()
    }

    private fun loadAlerts(){
        thread {
            val alerts = stopDataHolder?.getStop()?.id?.let { database.stopDao().getStop(it).alerts }
            activity?.runOnUiThread{
                if (alerts != null) {
                    adapter.update(alerts)
                }
            }
        }
    }

    private fun initFab(){
        binding.fab2.setOnClickListener{
            AddAlertDialogFragment().show(childFragmentManager, AddAlertDialogFragment::class.java.simpleName)
        }
    }

    override fun onAlertCreated(alert: Alert) {
        thread {
            val stop = stopDataHolder?.getStop()?.id?.let { database.stopDao().getStop(it) }
            stop?.alerts?.add(alert)
            if (stop != null) {
                database.stopDao().update(stop)
            }
            activity?.runOnUiThread {
                adapter.addAlert(alert)
            }
        }


    }

    override fun onAlertRemoved(alert: Alert) {
        thread {
            val stop = stopDataHolder?.getStop()?.id?.let { database.stopDao().getStop(it) }
            stop?.alerts?.remove(alert)
            if (stop != null) {
                database.stopDao().update(stop)
            }
            activity?.runOnUiThread {
                adapter.removeAlert(alert)
            }
        }
    }
}
