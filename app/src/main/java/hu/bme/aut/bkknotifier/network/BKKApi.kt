package hu.bme.aut.bkknotifier.network

import hu.bme.aut.bkknotifier.model.StopData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BKKApi {
    @GET("arrivals-and-departures-for-stop.json")
    fun getStopData(
        @Query("key") api_key: String?,
        @Query("includeReferences") references: String?,
        @Query("stopId") stopId: String?,
        @Query("onlyDepartures") onlyDepartures: String?,
        @Query("minutesBefore") minutesBefore: Int,
        @Query("minutesAfter") minutesAfter: Int,
        @Query("limit") limit: Int
    ): Call<StopData?>?
}