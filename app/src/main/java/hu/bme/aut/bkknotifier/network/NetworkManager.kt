package hu.bme.aut.bkknotifier.network

import hu.bme.aut.bkknotifier.model.StopData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val bkkApi: BKKApi

    private const val BASE_URL = "https://futar.bkk.hu/api/query/v1/ws/otp/api/where/"
    private const val API_KEY = "6488d125-a9b9-4189-8d5f-fe6371b38ff1"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        bkkApi = retrofit.create(BKKApi::class.java)
    }

    fun getStopData(stopId: String?): Call<StopData?>? {
        return bkkApi.getStopData(API_KEY, "routes,trips,stops", stopId, "true", 0, 120, 50)
    }

}