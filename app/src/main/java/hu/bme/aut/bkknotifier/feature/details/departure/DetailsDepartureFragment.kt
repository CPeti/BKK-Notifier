package hu.bme.aut.bkknotifier.feature.details.departure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.FragmentDetailsDepartureBinding
import hu.bme.aut.bkknotifier.feature.details.StopDataHolder
import hu.bme.aut.bkknotifier.model.StopData
import hu.bme.aut.bkknotifier.network.DataService


class DetailsDepartureFragment : Fragment() {
    private lateinit var binding: FragmentDetailsDepartureBinding
    private lateinit var adapter: DepartureAdapter
    private var stopDataHolder: StopDataHolder? = null

    companion object {
        const val   ActionTag = "DepartureActionTag"
    }

    private val updateUIReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val data = intent?.getSerializableExtra("DATA")
            if(data != null) {
                process((data as StopData))
            }
        }
    }
    fun process(data: StopData?) {
        adapter.updateDepartureData(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopDataHolder = if (activity is StopDataHolder) {
            activity as StopDataHolder?
        } else {
            throw RuntimeException(
                "Activity must implement StopDataHolder interface!"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        IntentFilter(stopDataHolder?.getStop()?.id ?: "null").also {
            this.context?.registerReceiver(updateUIReceiver, it)
        }

        val serviceIntent= Intent(context, DataService::class.java)
        serviceIntent.putExtra("id", stopDataHolder?.getStop()?.id)
        context?.startService(serviceIntent)
    }
    
    private fun initRecyclerView(stopDataHolder: StopDataHolder?){
        binding.departureRecyclerView.layoutManager = LinearLayoutManager(this.context)
        val dividerItemDecoration = DividerItemDecoration(
            binding.departureRecyclerView.context,
            (binding.departureRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        this.context?.let { it -> ContextCompat.getDrawable(it, R.drawable.divider)?.let { dividerItemDecoration.setDrawable(it) } }
        binding.departureRecyclerView.addItemDecoration(dividerItemDecoration)
        adapter = DepartureAdapter()
        adapter.updateDepartureData(stopDataHolder?.getStopData())
        binding.departureRecyclerView.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentDetailsDepartureBinding.inflate(LayoutInflater.from(context))
        initRecyclerView(stopDataHolder)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        IntentFilter(DataService.ActionTag).also {
            this.context?.unregisterReceiver(updateUIReceiver)
        }

        val intentSent = Intent()
        intentSent.action = ActionTag
        intentSent.putExtra("id", stopDataHolder?.getStop()?.id)
        context?.sendBroadcast(intentSent)
    }
}
