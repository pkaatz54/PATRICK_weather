package com.example.patrick_weather

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.patrick_weather.network.Weather
import com.example.patrick_weather.viewmodel.WeatherViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

//Patrick Kaatz
//991288014
//Assignment 3

class MainActivity : AppCompatActivity() {
    private val TAG = this@MainActivity.toString()
    private lateinit var weatherViewModel: WeatherViewModel
    private var baseUrl = "https://api.aerisapi.com/observations/closest?p="
    private lateinit var locationManager: LocationManager
    private lateinit var location : Location
    private lateinit var currentLocation : LatLng
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        weatherViewModel = WeatherViewModel()

        this.locationManager = LocationManager(this@MainActivity)
        this.currentLocation = LatLng(0.0, 0.0)

        if (LocationManager.locationPermissionGranted){
            this.getLastLocation()
        }



        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?: return

                for (location in locationResult.locations){
                    currentLocation = LatLng(location.latitude, location.longitude)

                    val current = LocalDateTime.now()

                    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    val formatted = current.format(formatter)

                    tvCurrentDate.setText(formatted)

                    getWeatherInfo(location.latitude.toString(), location.longitude.toString())

                    Log.e(TAG, "current location : " + currentLocation.toString())


                }

            }


        }
    }

    override fun onResume() {
        super.onResume()
        locationManager.requestLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        locationManager.fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }


    private fun getLastLocation() {
        this.locationManager.getLastLocation()?.observe(this, {loc : Location? ->
            if (loc!=null){
                this.location = loc
                this.currentLocation = LatLng(location.latitude, location.longitude)



                Log.e(TAG, "current location : " + this.currentLocation.toString())
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LocationManager.LOCATION_PERMISSION_REQUEST_CODE -> {
                LocationManager.locationPermissionGranted = (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)

                if (LocationManager.locationPermissionGranted){
                    //location available
                    //try to fetch the location

                    this.getLastLocation()
                }
                return
            }
        }
    }

    private fun getWeatherInfo(lat: String, lon: String){

        val apiUrl = this.baseUrl + lat + "," + lon + "&limit=1?query&client_id=gA3mWJHz2ISn5cfbaTyQk&client_secret=bkKoZQHWR6MmIhnNUmHTCeispwGB6IbmYkZCSFNa"

        Log.e(TAG, "apiUrl: " + apiUrl)

        this.weatherViewModel.getWeatherInfo(apiUrl)

        this.weatherViewModel.response.observe(this, {
            Log.e(TAG, "weather response : " + it.toString())
            this.displayWeatherData(it)


        })

    }

    private fun displayWeatherData(weather: Weather) {
        if(weather.response !=null){
            with(weather.response){

                if(this.isEmpty()){
                    tvWind.setText("N/a")
                    tvVisibility.setText("N/a")
                    tvHumidity.setText("N/a")
                    tvFeelsLike.setText("N/a")
                    tvTemperature.setText("N/a")

                    return


                }

                if(this[0].ob?.tempC !=null){
                    Log.e(TAG, "tempC: " + this[0].ob?.tempC.toString() )
                    tvTemperature.setText(weather.response[0].ob?.tempC.toString() + "\u2103C")

                }

                if(this[0].ob?.feelslikeC !=null){
                    tvFeelsLike.setText("Feels Like " + weather.response[0].ob?.feelslikeC.toString() + "\u2103C")

                }

                if(this[0].ob?.humidity !=null){
                    tvHumidity.setText("Humidity: " + weather.response[0].ob?.humidity.toString() + "%")

                }

                if(this[0].ob?.visibilityKM !=null){
                    tvVisibility.setText("Visibility: " + weather.response[0].ob?.visibilityKM.toString() + " km")

                }

                if(this[0].ob?.windKPH !=null){
                    tvWind.setText("Wind Speed: " + weather.response[0].ob?.windKPH.toString() + " kph - " + this[0].ob?.windDir)

                }


            }

        }


    }
}