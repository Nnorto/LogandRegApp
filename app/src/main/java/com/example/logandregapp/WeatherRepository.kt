package com.example.logandregapp

// WeatherRepository.kt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val BASE_URL = "https://api.openweathermap.org/"
    private val API_KEY = "20f68df044c1ace4fedd3aac1a85cbf0"
    private val weatherService: WeatherService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherService = retrofit.create(WeatherService::class.java)
    }

    fun getWeatherByCity(cityName: String, callback: retrofit2.Callback<WeatherResponse>) {
        val call = weatherService.getCurrentWeather(cityName, API_KEY)
        call.enqueue(callback)
    }

    fun getWeatherByLocation(lat: Double, lon: Double, callback: retrofit2.Callback<WeatherResponse>) {
        val call = weatherService.getCurrentWeatherByLocation(lat, lon, API_KEY)
        call.enqueue(callback)
    }
}