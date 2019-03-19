package com.example.xenya.openweather.view

import com.example.xenya.openweather.entities.City

interface MainView {
    fun showError()
    fun showLoading()
    fun hideLoading()
    fun showCities(cities: List<City>)
    fun checkLocationPermission()
    fun navigateToDetailsView(cityId: Int)
}
