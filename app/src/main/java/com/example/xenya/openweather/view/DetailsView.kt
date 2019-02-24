package com.example.xenya.openweather.view

import com.example.xenya.openweather.entities.City

interface DetailsView {
    fun showError()
    fun showContent(city: City)
}
