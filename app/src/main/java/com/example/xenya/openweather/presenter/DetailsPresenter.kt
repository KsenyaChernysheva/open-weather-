package com.example.xenya.openweather.presenter

import android.content.Context
import com.example.xenya.openweather.database.AppDatabase
import com.example.xenya.openweather.model.WeatherModel
import com.example.xenya.openweather.view.DetailsView
import io.reactivex.rxkotlin.subscribeBy

class DetailsPresenter(
        var view: DetailsView?,
        val cityId: Int
) {
    private var model: WeatherModel? = null

    constructor(view: DetailsView?, cityId: Int, context: Context) : this(view, cityId) {
        model = WeatherModel(AppDatabase.getInstance(context))

        model?.let {
            it.getCityFromDbById(cityId)
                    .subscribeBy(onSuccess = {
                        if (it.sys?.country.isNullOrEmpty()) {
                            it.sys?.country = "DIO"
                        }
                        view?.showContent(it)
                    }, onError = {
                        view?.showError()
                    })
        }
    }

    fun destroyView() {
        view = null
    }
}
