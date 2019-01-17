package com.example.xenya.openweather.entities

import com.google.gson.annotations.SerializedName

data class Clouds(
        @SerializedName("all")
        var all: Int? = 0
)
