package com.example.walletweather.dataClasses

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val link: String
)