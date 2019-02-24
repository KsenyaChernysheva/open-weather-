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
import com.example.xenya.openweather.entities.City
import com.example.xenya.openweather.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    var presenter: MainPresenter? = null

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 21
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, this)

        presenter?.onCreateView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter?.onLocationAccessGranted(getLocation())
                } else {
                    presenter?.onLocationAccessNotGranted()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroyView()
    }

    override fun showError() = Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

    override fun showLoading() {
        pb_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb_loading.visibility = View.GONE
    }

    override fun showCities(cities: List<City>) {
        rv_cities.adapter = WeatherListAdapter(cities) {
            presenter?.onCityClick(it)
        }
    }

    override fun navigateToDetailsView(cityId: Int) {
        startActivity(DetailsActivity.getIntent(this, cityId))
    }

    override fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_LOCATION)
        } else {
            presenter?.onLocationAccessGranted(getLocation())
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(): Location? {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager?.let {
            it.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    ?: it.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        }
    }
}
