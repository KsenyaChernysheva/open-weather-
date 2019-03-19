package com.example.xenya.openweather

import android.app.Application
import com.example.xenya.openweather.dagger.AppComponent
import com.example.xenya.openweather.dagger.DaggerAppComponent
import com.example.xenya.openweather.database.DatabaseModule
import com.example.xenya.openweather.model.ModelModule
import com.example.xenya.openweather.network.WeatherModule

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .weatherModule(WeatherModule())
                .databaseModule(DatabaseModule(context = applicationContext))
                .modelModule(ModelModule())
                .build()
    }
}
