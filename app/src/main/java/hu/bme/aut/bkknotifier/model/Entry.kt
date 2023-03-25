package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Entry (

  @SerializedName("stopId"        ) var stopId        : String?              = null,
  @SerializedName("routeIds"      ) var routeIds      : ArrayList<String>    = arrayListOf(),
  @SerializedName("alertIds"      ) var alertIds      : ArrayList<String>    = arrayListOf(),
  @SerializedName("nearbyStopIds" ) var nearbyStopIds : ArrayList<String>    = arrayListOf(),
  @SerializedName("stopTimes"     ) var stopTimes     : ArrayList<StopTime> = arrayListOf()

) : Serializable