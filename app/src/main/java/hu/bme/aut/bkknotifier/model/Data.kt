package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Data (

  @SerializedName("limitExceeded" ) var limitExceeded : Boolean? = null,
  @SerializedName("entry"         ) var entry         : Entry?   = Entry(),
  @SerializedName("references"    ) var references    : References? = References(),
  @SerializedName("class"         ) var _class         : String?  = null

) : Serializable