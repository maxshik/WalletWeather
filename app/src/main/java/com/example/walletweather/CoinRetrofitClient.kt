import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinRetrofitClient {
    private const val BASE_URL = "https://api.coincap.io/"

    val instance: CoinApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(CoinApi::class.java)
    }
}