import com.example.walletweather.dataClasses.CoinResponse
import retrofit2.http.GET

interface CoinApi {
    @GET("v2/assets")
    suspend fun getCoins(): CoinResponse
}