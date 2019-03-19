package com.example.xenya.openweather.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.xenya.openweather.App
import com.example.xenya.openweather.model.WeatherModel
import com.example.xenya.openweather.view.DetailsView
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@InjectViewState
class DetailsPresenter(
        val cityId: Int
) : MvpPresenter<DetailsView>() {

    @Inject
    lateinit var model: WeatherModel

    override fun onFirstViewAttach() {
        model.let {
            it.getCityFromDbById(cityId)
                    .subscribeBy(onSuccess = {
                        if (it.sys?.country.isNullOrEmpty()) {
                            it.sys?.country = "DIO"
                        }
                        viewState.showContent(it)
                    }, onError = {
                        viewState.showError()
                    })
        }
    }

    fun onClickButton() = viewState.navigateToForecast(cityId)

    init {
        App.appComponent.inject(this)
    }
}
