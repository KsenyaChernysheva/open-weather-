package com.example.xenya.openweather.entities

import com.google.gson.annotations.SerializedName

data class City(
        @SerializedName("clouds")
        var clouds: Clouds? = Clouds(),
        @SerializedName("coord")
        var coord: Coord? = Coord(),
        @SerializedName("dt")
        var dt: Int? = 0,
        @SerializedName("id")
        var id: Int? = 0,
        @SerializedName("main")
        var main: Main? = Main(),
        @SerializedName("name")
        var name: String? = "",
        @SerializedName("rain")
        var rain: Any? = Any(),
        @SerializedName("snow")
        var snow: Any? = Any(),
        @SerializedName("sys")
        var sys: Sys? = Sys(),
        @SerializedName("weather")
        var weather: List<Weather?>? = listOf(),
        @SerializedName("wind")
        var wind: Wind? = Wind()
)
