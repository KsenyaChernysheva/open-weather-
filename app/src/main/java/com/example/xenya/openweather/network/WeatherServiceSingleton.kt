package com.example.xenya.openweather.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object WeatherServiceSingleton {
    private var instance: WeatherService? = null

    fun getInstance(): WeatherService {
        return instance ?: getService().also {
            instance = it
        }
    }

    private fun getService(): WeatherService =
            Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(WeatherService::class.java)
}
