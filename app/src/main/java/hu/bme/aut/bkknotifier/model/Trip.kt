package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Trip (

  @SerializedName("id"                   ) var id                   : String?  = null,
  @SerializedName("routeId"              ) var routeId              : String?  = null,
  @SerializedName("shapeId"              ) var shapeId              : String?  = null,
  @SerializedName("blockId"              ) var blockId              : String?  = null,
  @SerializedName("tripHeadsign"         ) var tripHeadsign         : String?  = null,
  @SerializedName("serviceId"            ) var serviceId            : String?  = null,
  @SerializedName("directionId"          ) var directionId          : String?  = null,
  @SerializedName("bikesAllowed"         ) var bikesAllowed         : Boolean? = null,
  @SerializedName("wheelchairAccessible" ) var wheelchairAccessible : Boolean? = null

) : Serializable