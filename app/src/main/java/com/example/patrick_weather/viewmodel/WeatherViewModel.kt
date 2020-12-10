package com.example.patrick_weather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patrick_weather.network.Weather
import com.example.patrick_weather.network.WeatherApi
import kotlinx.coroutines.launch
import java.lang.Exception

//Patrick Kaatz
//991288014
//Assignment 3
class WeatherViewModel : ViewModel() {
    private val weather = MutableLiveData<Weather>()

    val response: LiveData<Weather>
        get() = weather

    fun getWeatherInfo(apiUrl : String){
        viewModelScope.launch {
            try{
                val weather = WeatherApi.RETROFIT_SERVICE_WEATHER.retrieveResponse(apiUrl)

                this@WeatherViewModel.weather.postValue(weather)
            }catch (ex: Exception){
                Log.e("WeatherViewModel", ex.localizedMessage)
            }

        }

    }

}