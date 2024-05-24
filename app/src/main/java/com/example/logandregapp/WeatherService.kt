package com.example.logandregapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    fun getCurrentWeather(@Query("q") cityName: String, @Query("appid") apiKey: String): Call<WeatherResponse>

    @GET("data/2.5/weather")
    fun getCurrentWeatherByLocation(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") apiKey: String): Call<WeatherResponse>
}

