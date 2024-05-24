package com.example.logandregapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.logandregapp.ui.theme.LogandRegAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : ComponentActivity() {
    private lateinit var weatherRepository: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherRepository = WeatherRepository()
        setContent {
            LogandRegAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(weatherRepository)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WeatherScreen(weatherRepository: WeatherRepository) {
    var cityName by remember { mutableStateOf("Moscow") }
    var weatherInfo by remember { mutableStateOf("") }
    var weatherIconUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var inputCityName by remember { mutableStateOf(cityName) }

    fun fetchWeather(city: String) {
        weatherRepository.getWeatherByCity(city, object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val weatherResponse = response.body()!!
                    val tempCelsius = weatherResponse.main.temp - 273.15
                    weatherInfo = "Температура: ${"%.2f".format(tempCelsius)}°C\n" +
                            "Влажность: ${weatherResponse.main.humidity}%\n" +
                            "Описание: ${weatherResponse.weather[0].description}"
                    weatherIconUrl = "https://openweathermap.org/img/w/${weatherResponse.weather[0].icon}.png"
                } else {
                    errorMessage = "Ошибка: ${response.code()} ${response.message()}"
                    Log.e("WeatherScreen", errorMessage)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                errorMessage = "Ошибка: ${t.message}"
                Log.e("WeatherScreen", errorMessage)
            }
        })
    }

    LaunchedEffect(cityName) {
        fetchWeather(cityName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Погода в городе", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = inputCityName,
            onValueChange = { inputCityName = it },
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { cityName = inputCityName }) {
            Text("Обновить")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (weatherInfo.isNotEmpty()) {
            Text(text = weatherInfo, style = MaterialTheme.typography.bodyLarge)
        } else if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (weatherIconUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(data = weatherIconUrl),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}
// 12345678a
// test@gmail.com
@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    LogandRegAppTheme {
        WeatherScreen(weatherRepository = WeatherRepository())
    }
}
