package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class StopTime (

  @SerializedName("stopHeadsign"           ) var stopHeadsign           : String?  = null,
  @SerializedName("departureTime"          ) var departureTime          : Long?     = null,
  @SerializedName("predictedDepartureTime" ) var predictedDepartureTime : Long?     = null,
  @SerializedName("tripId"                 ) var tripId                 : String?  = null,
  @SerializedName("serviceDate"            ) var serviceDate            : String?  = null,
  @SerializedName("mayRequireBooking"      ) var mayRequireBooking      : Boolean? = null

) : Serializable