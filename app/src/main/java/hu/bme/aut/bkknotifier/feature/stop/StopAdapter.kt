package hu.bme.aut.bkknotifier.feature.stop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.ItemStopBinding
import hu.bme.aut.bkknotifier.feature.stop.data.Stop
import hu.bme.aut.bkknotifier.network.DataService

class StopAdapter(private val listener: OnStopSelectedListener) : RecyclerView.Adapter<StopAdapter.StopViewHolder>(){
    private val stops: MutableList<Stop> = ArrayList()
    private lateinit var context: Context

    companion object {
        const val   ActionTag = "ToggleActionTag"
    }

    interface OnStopSelectedListener {
        fun onStopSelected(stop: Stop?)
        fun onStopRemoved(stop: Stop)
        fun onSwitched(stop: Stop, state: Boolean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stop, parent, false)
        context = parent.context
        return StopViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newStops: List<Stop>) {
        stops.clear()
        stops.addAll(newStops)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        val item = stops[position]
        holder.bind(item)
        holder.binding.StopRemoveButton.setOnClickListener {
            listener.onStopRemoved(stops[position])
        }
        holder.binding.StopSwitch.setOnClickListener {
            listener.onSwitched(stops[position], holder.binding.StopSwitch.isChecked)
        }
    }

    fun addStop(newStop: Stop){
        stops.add(newStop)
        notifyItemInserted(stops.size - 1)
    }

    fun handleSwitchAction(stop: Stop, state: Boolean){
        if(state){
            startDataService(stop)
        } else {
            stopDataService(stop)
        }
    }

    fun initService(preContext: Context){
        context = preContext
        for(stop in stops){
            if(stop.toggle){
                startDataService(stop)
            }
        }
    }

    inner class StopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ItemStopBinding.bind(itemView)
        private var item: Stop? = null

        init {
            binding.root.setOnClickListener{ listener.onStopSelected(item) }
        }

        fun bind(newStop: Stop?) {
            item = newStop
            binding.StopNameTextView.text = item?.name ?: "error"
            binding.StopSwitch.isChecked = item?.toggle ?: false
        }
    }

    override fun getItemCount(): Int = stops.size

    private fun startDataService(stop: Stop){
        val serviceIntent= Intent(context, DataService::class.java)
        serviceIntent.putExtra("id", stop.id)
        context.startService(serviceIntent)
    }

    private fun stopDataService(stop: Stop){
        val intentSent = Intent()
        intentSent.action = ActionTag
        intentSent.putExtra("id", stop.id)
        context.sendBroadcast(intentSent)
    }

    fun contains(newStop: Stop) : Boolean{
        for(stop in stops){
            if(newStop.id == stop.id){
                return true
            }
        }
        return false
    }

    fun removeStop(stop: Stop) {
        stopDataService(stop)
        stops.remove(stop)
    }
}