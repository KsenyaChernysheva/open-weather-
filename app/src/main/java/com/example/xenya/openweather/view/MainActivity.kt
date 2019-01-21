package com.example.xenya.openweather.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.xenya.openweather.R
import com.example.xenya.openweather.database.AppDatabase
import com.example.xenya.openweather.network.WeatherServiceSingleton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_LOCATION = 21
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onLocationProvided(getLocation())
                } else {
                    onLocationProvided(kazanLocation())
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_LOCATION)
        } else {
            onLocationProvided(getLocation())
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(): Location {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager?.let {
            it.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    ?: it.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                    ?: kazanLocation()
        } ?: kazanLocation()
    }

    private fun kazanLocation(): Location {
        return Location("").apply {
            latitude = 55.792115
            longitude = 49.112339
        }
    }

    private fun onLocationProvided(location: Location) {
        WeatherServiceSingleton.getInstance()
                .findByLocation(location.latitude, location.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { it.list }
                .map {
                    AppDatabase.getInstance(this)
                            .getCityDao()
                            .saveAll(it)
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { pb_loading.visibility = View.VISIBLE }
                .doAfterTerminate { pb_loading.visibility = View.GONE }
                .subscribeBy(onSuccess = {
                    rv_cities.adapter = WeatherListAdapter(it) {
                        startActivity(DetailsActivity.getIntent(this, it))
                    }
                }, onError = {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                })
    }
}
