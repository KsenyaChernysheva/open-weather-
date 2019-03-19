package com.example.xenya.openweather.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.xenya.openweather.App
import com.example.xenya.openweather.model.WeatherModel
import com.example.xenya.openweather.view.ForecastView
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@InjectViewState
class ForecastPresenter(
        val cityId: Int
) : MvpPresenter<ForecastView>() {
    @Inject
    lateinit var model: WeatherModel

    override fun onFirstViewAttach() {
        model.let {
            it.loadForecastById(cityId)
                    .doOnSubscribe { viewState.showLoading() }
                    .doAfterTerminate { viewState.hideLoading() }
                    .subscribeBy(onSuccess = {
                        viewState.showForecast(it)
                    }, onError = {
                        viewState.showError()
                    })
        }
    }

    init {
        App.appComponent.inject(this)
    }
}
