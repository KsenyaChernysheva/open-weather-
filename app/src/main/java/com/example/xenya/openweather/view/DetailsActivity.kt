package com.example.xenya.openweather.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xenya.openweather.R
import com.example.xenya.openweather.database.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CITY_ID = "cityid"

        fun getIntent(context: Context, cityId: Int) =
                Intent(context, DetailsActivity::class.java).apply {
                    putExtra(EXTRA_CITY_ID, cityId)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val cityId: Int = intent.getIntExtra(EXTRA_CITY_ID, 0)
        AppDatabase.getInstance(this)
                .getCityDao()
                .getCityById(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    val country = if (it.sys?.country.isNullOrEmpty()) {
                        "DIO"
                    } else {
                        it.sys?.country
                    }
                    tv_city.text = "${it.name}, $country"
                    tv_humidity.text = it.main.humidity.toString()
                    tv_pressure.text = it.main.pressure.toString()
                    tv_temperature.text = it.main.temp.toString()
                    tv_wind.text = it.wind?.speed.toString()
                }, onError = {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                })
    }
}

