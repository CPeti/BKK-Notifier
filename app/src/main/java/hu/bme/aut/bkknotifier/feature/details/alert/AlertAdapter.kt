package hu.bme.aut.bkknotifier.feature.details.alert

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.ItemAlertBinding
import hu.bme.aut.bkknotifier.feature.stop.data.Alert

class AlertAdapter(private val listener: OnAlertSelectedListener) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>(){
    private var alerts: MutableList<Alert> = ArrayList()
    private lateinit var context: Context

    interface OnAlertSelectedListener {
        fun onAlertRemoved(alert: Alert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newAlerts: ArrayList<Alert>){
        alerts = newAlerts
        notifyDataSetChanged()
    }

    fun addAlert(newAlert: Alert){
        alerts.add(newAlert)
        notifyItemInserted(alerts.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAlert(oldAlert: Alert){
        alerts.remove(oldAlert)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val item = alerts[position]
        holder.bind(item)
        holder.binding.AlertRemoveButton.setOnClickListener {
            listener.onAlertRemoved(alerts[position])
        }
    }

    inner class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ItemAlertBinding.bind(itemView)
        private var item: Alert? = null

        fun bind(newAlert: Alert?) {
            item = newAlert

            binding.alertShortNameTextView.text = context.getString(R.string.alert, item?.time, item?.shortName)

        }
    }

    override fun getItemCount(): Int = alerts.size
}