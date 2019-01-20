package com.example.xenya.openweather.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.xenya.openweather.R

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
        Log.e("LOCATION", "lat: ${location.latitude} long: ${location.longitude}")
    }
}
