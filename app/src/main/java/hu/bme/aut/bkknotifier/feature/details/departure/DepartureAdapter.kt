package hu.bme.aut.bkknotifier.feature.details.departure

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.ItemDepartureBinding
import hu.bme.aut.bkknotifier.model.Route
import hu.bme.aut.bkknotifier.model.StopData
import hu.bme.aut.bkknotifier.model.StopTime
import hu.bme.aut.bkknotifier.model.Trip

class DepartureAdapter : RecyclerView.Adapter<DepartureAdapter.DepartureViewHolder>(){
    private var stopData: StopData? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_departure, parent, false)
        return DepartureViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartureViewHolder, position: Int) {
        val item = stopData?.data?.entry?.stopTimes?.get(position)
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDepartureData(data: StopData?){
        stopData = data
        notifyDataSetChanged()
    }

    inner class DepartureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var binding = ItemDepartureBinding.bind(itemView)
        private var item: StopTime? = null
        private var trip: Trip? = null
        private var route: Route? = null

        fun bind(newDeparture: StopTime?) {
            item = newDeparture
            trip = stopData?.data?.references?.trips?.get(item?.tripId)
            route = stopData?.data?.references?.routes?.get(trip?.routeId)

            binding.departureHeadsignTextView.text = item?.stopHeadsign
            binding.departureRouteTextView.text = route?.shortName
            if(route?.color != null){
                binding.departureRouteTextView.setTextColor(Color.parseColor("#" + route?.textColor))
                (binding.departureRouteTextView.background as GradientDrawable).setColor(Color.parseColor("#" + route?.color))
            }
            binding.departureTimeTextView.text = itemView.context.getString(R.string.time, (stopData?.currentTime?.div(1000)?.let {
                (item?.predictedDepartureTime ?: item?.departureTime)?.minus(it)
            })?.div(60)?.plus(1).toString())
        }
    }

    override fun getItemCount(): Int = stopData?.data?.entry?.stopTimes?.size ?: 0


}
