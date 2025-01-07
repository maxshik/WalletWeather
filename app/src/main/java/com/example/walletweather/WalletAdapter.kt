
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.walletweather.R
import com.example.walletweather.dataClasses.Coin
import com.example.walletweather.databinding.CurrencyCardBinding
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat

class CoinAdapter(
    var coins: MutableList<Coin>,
) : RecyclerView.Adapter<CoinAdapter.CoinHolder>() {

    class CoinHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = CurrencyCardBinding.bind(item)

        private val coinImages = mapOf(
            "Bitcoin" to "https://bin.bnbstatic.com/image/admin_mgs_image_upload/20201110/87496d50-2408-43e1-ad4c-78b47b448a6a.png",
            "Ethereum" to "https://bin.bnbstatic.com/image/admin_mgs_image_upload/20201110/3a8c9fe6-2a76-4ace-aa07-415d994de6f0.png",
            "Tether" to "https://bin.bnbstatic.com/image/admin_mgs_image_upload/20220218/94863af2-c980-42cf-a139-7b9f462a36c2.png",
            "XRP" to "https://bin.bnbstatic.com/image/admin_mgs_image_upload/20201110/4766a9cc-8545-4c2b-bfa4-cad2be91c135.png",
            "Solana" to "https://bin.bnbstatic.com/image/admin_mgs_image_upload/20230404/b2f0c70f-4fb2-4472-9fe7-480ad1592421.png"
        )

        fun bind(coin: Coin) = with(binding) {
            currencyName.text = coin.name
            val decimalFormat = DecimalFormat("#.#")
            price.text = "${decimalFormat.format(coin.priceUsd.toFloat())}$"
            boost.text = "${decimalFormat.format(coin.changePercent24Hr.toFloat())}$"
            when {
                coin.changePercent24Hr.toFloat() > 0 -> {
                    boost.text = "+${decimalFormat.format(coin.changePercent24Hr.toFloat())}$"
                    boost.setTextColor(Color.parseColor("#008800"))
                }
                coin.changePercent24Hr.toFloat() < 0 -> {
                    boost.text = "${decimalFormat.format(coin.changePercent24Hr.toFloat())}$"
                    boost.setTextColor(Color.parseColor("#FF0000"))
                }
                else -> {
                    boost.text = "0.0$"
                    boost.setTextColor(Color.parseColor("#0000FF"))
                }
            }

            val imageUrl = coinImages[coin.name]
            Picasso.get().load(imageUrl).into(cryptoImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_card, parent, false)
        return CoinHolder(view)
    }

    override fun getItemCount(): Int = coins.size

    override fun onBindViewHolder(holder: CoinHolder, position: Int) {
        val coin = coins[position]
        holder.bind(coin)
    }
}