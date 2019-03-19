package com.example.xenya.openweather.dagger

import com.example.xenya.openweather.database.DatabaseModule
import com.example.xenya.openweather.model.ModelModule
import com.example.xenya.openweather.network.WeatherModule
import com.example.xenya.openweather.presenter.DetailsPresenter
import com.example.xenya.openweather.presenter.ForecastPresenter
import com.example.xenya.openweather.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WeatherModule::class, DatabaseModule::class, ModelModule::class])
interface AppComponent {
    fun inject(activity: DetailsPresenter)
    fun inject(activity: ForecastPresenter)
    fun inject(mainPresenter: MainPresenter)
}
