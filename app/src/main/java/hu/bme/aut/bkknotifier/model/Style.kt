package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Style (

  @SerializedName("color    " ) var color     : String? = null

) : Serializable