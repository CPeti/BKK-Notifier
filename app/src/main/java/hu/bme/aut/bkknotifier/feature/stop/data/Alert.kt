package hu.bme.aut.bkknotifier.feature.stop.data

import androidx.room.ColumnInfo
import java.io.Serializable

data class Alert(
    @ColumnInfo(name = "shortname") var shortName: String,
    @ColumnInfo(name = "time"     ) var time: Int,
) : Serializable
