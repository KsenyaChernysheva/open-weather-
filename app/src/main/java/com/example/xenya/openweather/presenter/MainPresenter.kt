package com.example.xenya.openweather.presenter

import android.content.Context
import android.location.Location
import com.example.xenya.openweather.database.AppDatabase
import com.example.xenya.openweather.model.WeatherModel
import com.example.xenya.openweather.view.MainView
import io.reactivex.rxkotlin.subscribeBy

class MainPresenter(var view: MainView?) {
    private var model: WeatherModel? = null

    constructor(view: MainView?, context: Context) : this(view) {
        model = WeatherModel(AppDatabase.getInstance(context))
    }

    fun onCreateView() {
        view?.checkLocationPermission()
    }

    fun onLocationAccessGranted(location: Location?) = loadWeatherByLocation(location)

    fun onLocationAccessNotGranted() = loadWeatherByLocation(null)

    fun onCityClick(cityId: Int) = view?.navigateToDetailsView(cityId)

    fun onDestroyView() {
        view = null
    }

    private fun loadWeatherByLocation(location: Location?) {
        model?.let {
            val notNullLocation: Location = location ?: it.getKazanLocation()
            it.loadWeatherByLocation(notNullLocation.latitude, notNullLocation.longitude)
                    .doOnSubscribe { view?.showLoading() }
                    .doAfterTerminate { view?.hideLoading() }
                    .subscribeBy(onSuccess = {
                        view?.showCities(it)
                    }, onError = {
                        view?.showError()
                    })
        }

    }
}
