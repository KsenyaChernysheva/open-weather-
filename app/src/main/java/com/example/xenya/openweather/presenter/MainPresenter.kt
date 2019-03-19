package com.example.xenya.openweather.presenter

import android.location.Location
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.xenya.openweather.App
import com.example.xenya.openweather.model.WeatherModel
import com.example.xenya.openweather.view.MainView
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var model: WeatherModel

    override fun onFirstViewAttach() {
        viewState.checkLocationPermission()
    }

    fun onLocationAccessGranted(location: Location?) = loadWeatherByLocation(location)

    fun onLocationAccessNotGranted() = loadWeatherByLocation(null)

    fun onCityClick(cityId: Int) = viewState.navigateToDetailsView(cityId)

    private fun loadWeatherByLocation(location: Location?) {
        model.let { weatherModel ->
            val notNullLocation: Location = location ?: weatherModel.getKazanLocation()
            weatherModel.loadWeatherByLocation(notNullLocation.latitude, notNullLocation.longitude)
                    .doOnSubscribe { viewState.showLoading() }
                    .doAfterTerminate { viewState.hideLoading() }
                    .subscribeBy(onSuccess = {
                        viewState.showCities(it)
                    }, onError = {
                        weatherModel.getCitiesFromDb().subscribeBy {
                            viewState.showCities(it)
                        }
                        viewState.showError()
                    })
        }

    }
}
