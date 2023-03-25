package hu.bme.aut.bkknotifier.feature.stop.data

import java.io.Serializable
import androidx.room.*

@Entity(tableName = "stop")
data class Stop (
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "toggle") var toggle: Boolean,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false)   var id: String,
    @ColumnInfo(name = "alerts") var alerts: ArrayList<Alert> = arrayListOf()
) : Serializable

