package com.example.walletweather

import CoinAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walletweather.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        getCurrencyData()
        getWeatherData("Minsk")
    }

    private fun init() {
        adapter = CoinAdapter(mutableListOf())
        binding.rcViewOfContacts.layoutManager = LinearLayoutManager(this)
        binding.rcViewOfContacts.adapter = adapter
    }

    fun getWeatherData(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val apiKey = "694d590a3ce744aa7309ae838801b04b"
            val response = RetrofitClient.instance.getWeather(city, apiKey).execute()

            if (response.isSuccessful) {
                val weatherResponse = response.body()
                withContext(Dispatchers.Main) {
                    val decimalFormat = DecimalFormat("#.#")
                    val temp = weatherResponse?.main?.temp?.toDouble() ?: 0.0
                    val formattedTemp = decimalFormat.format(temp)

                    val prediction = when {
                        temp < 0 -> {
                            "Температура ниже нуля; все криптовалюты, вероятно, упадут. Биткоин особенно упадёт!!!"
                        }
                        temp in 0.0..5.0 -> {
                            "Биткойн и Солана будут расти."
                        }
                        temp in 6.0..10.0 -> {
                            "Эфириум может немного вырасти."
                        }
                        temp in 11.0..15.0 -> {
                            "Рынок нестабилен; следите за Эфириумом."
                        }
                        temp in 16.0..20.0 -> {
                            "Биткойн может упасть; подумайте о покупке Тетера."
                        }
                        temp > 20 -> {
                            "Инвестируйте в Эфириум; он может показать рост."
                        }
                        else -> {
                            "Рынок непредсказуем; следите за новыми трендами."
                        }
                    }

                    Log.d("WeatherData", "Температура: $formattedTemp°C")
                    Log.d("WeatherData", "Прогноз: $prediction")

                    binding.weatherInMinsk.text = "Сегодня в Минске $formattedTemp°C. $prediction"
                }
            } else {
                Log.e("WeatherData", "Ошибка: ${response.errorBody()?.string()}")
            }
        }
    }

    fun getCurrencyData() {
        val coinIds = listOf("bitcoin", "ethereum", "tether", "solana", "xrp")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = CoinRetrofitClient.instance.getCoins()
                val filteredCoins = response.data.filter { coin ->
                    coin.id in coinIds
                }

                withContext(Dispatchers.Main) {
                    adapter.coins.clear()
                    adapter.coins.addAll(filteredCoins)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("CurrencyData", "Ошибка получения данных: ${e.message}")
            }
        }
    }
}