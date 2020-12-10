package com.example.patrick_weather.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Patrick Kaatz
//991288014
//Assignment 3

@JsonClass(generateAdapter = true)

data class Weather(
    val response: Array<Response>? = null

)

data class Response(
    val ob: ob? = null

)
data class ob(
    val tempC: Float? = null,
    val feelslikeC: Float? = null,
    val humidity: Int? = null,
    val windKPH: Int? = null,
    val visibilityKM: Float? = null,
    val windDir: String? = null

)