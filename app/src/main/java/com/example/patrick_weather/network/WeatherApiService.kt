package com.example.patrick_weather.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

//Patrick Kaatz
//991288014
//Assignment 3



    private var baseUrl = "https://api.aerisapi.com/observations/"
    //“https://api.aerisapi.com/observations/LAT,LNG?query&client_id=gA3mWJHz2ISn5cfbaTyQk&client_secret=bkKoZQHWR6MmIhnNUmHTCeispwGB6IbmYkZCSFNa”

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(MoshiConverterFactory.create(moshi)).build()

interface WeatherApiService {
    @GET
    suspend fun retrieveResponse(@Url apiUrl: String): Weather
}

object WeatherApi{
    val RETROFIT_SERVICE_WEATHER : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }


}

