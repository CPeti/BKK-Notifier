package hu.bme.aut.bkknotifier.feature.details

import hu.bme.aut.bkknotifier.feature.stop.data.Stop
import hu.bme.aut.bkknotifier.model.StopData

interface StopDataHolder {
    fun getStopData(): StopData?
    fun loadStopData()
    fun getStop(): Stop
}