package com.example.cryptocurrencieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptocurrencieslist.databinding.ActivityCurrencyInfoBinding
import com.example.cryptocurrencieslist.retrofit.CryptoCompareApi
import com.example.cryptocurrencieslist.retrofit.data.USD
import com.example.cryptocurrencieslist.room.CurrenciesDB
import com.example.cryptocurrencieslist.utils.convertTime
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCurrencyInfoBinding
    private lateinit var currencyInfo: USD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_info)

        val db = CurrenciesDB.getInstance(this)

        binding = ActivityCurrencyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val currencyName = intent.getStringExtra("NAME")

        CoroutineScope(Dispatchers.IO).launch {
            currencyInfo = db.getDAO().getInfoByName(currencyName!!)
            runOnUiThread {
                binding.apply {
                    toSymbolTv.text = currencyInfo.TOSYMBOL
                    fromSymbolTv.text = currencyInfo.FROMSYMBOL
                    priceTv.text = currencyInfo.PRICE.toString()
                    minPriceTv.text = currencyInfo.LOWDAY.toString()
                    maxPriceTv.text = currencyInfo.HIGHDAY.toString()
                    lastOfferTv.text = currencyInfo.LASTMARKET
                    lastUpdateTv.text = convertTime(currencyInfo.LASTUPDATE)
                    Picasso.get().load(CryptoCompareApi.DEFAULT_URL + currencyInfo.IMAGEURL)
                        .into(currencyImage)
                }
            }
        }
    }
}