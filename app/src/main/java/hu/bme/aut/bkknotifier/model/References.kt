package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class References(

    @SerializedName("routes") var routes : Map<String?, Route?>? = emptyMap(),
    @SerializedName("trips" ) var trips  : Map<String?, Trip?>? = emptyMap(),
    @SerializedName("stops" ) var stops  : Map<String?, Stop?>? = emptyMap()

) : Serializable
