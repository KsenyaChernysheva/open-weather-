package com.example.xenya.openweather.model

import com.example.xenya.openweather.database.AppDatabase
import com.example.xenya.openweather.network.WeatherService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModelModule {

    @Provides
    @Singleton
    fun getWeatherModel(appDatadase: AppDatabase, weatherService: WeatherService): WeatherModel =
            WeatherModel(appDatadase, weatherService)
}
