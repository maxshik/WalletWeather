package com.example.walletweather.dataClasses

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)
