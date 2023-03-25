package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class StopData (

  @SerializedName("currentTime" ) var currentTime : Long?    = null,
  @SerializedName("version"     ) var version     : Int?    = null,
  @SerializedName("status"      ) var status      : String? = null,
  @SerializedName("code"        ) var code        : Int?    = null,
  @SerializedName("text"        ) var text        : String? = null,
  @SerializedName("data"        ) var data        : Data?   = Data()

) : Serializable