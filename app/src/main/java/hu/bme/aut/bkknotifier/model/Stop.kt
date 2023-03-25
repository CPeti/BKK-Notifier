package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Stop(
    @SerializedName("id"   ) var id              : String?  = null,
    @SerializedName("name" ) var name              : String?  = null,
) : Serializable
