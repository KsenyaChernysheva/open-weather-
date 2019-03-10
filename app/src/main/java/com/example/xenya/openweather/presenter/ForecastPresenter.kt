package com.example.xenya.openweather.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.xenya.openweather.database.AppDatabase
import com.example.xenya.openweather.model.WeatherModel
import com.example.xenya.openweather.view.ForecastView
import io.reactivex.rxkotlin.subscribeBy

@InjectViewState
class ForecastPresenter(
        val cityId: Int
) : MvpPresenter<ForecastView>() {
    private var model: WeatherModel? = null

    override fun onFirstViewAttach() {
        model?.let {
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

    constructor(cityId: Int, context: Context) : this(cityId) {
        model = WeatherModel(AppDatabase.getInstance(context))
    }
}
