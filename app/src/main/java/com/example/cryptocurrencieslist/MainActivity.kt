package com.example.cryptocurrencieslist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrencieslist.adapter.Currency
import com.example.cryptocurrencieslist.adapter.CurrencyAdapter
import com.example.cryptocurrencieslist.databinding.ActivityMainBinding
import com.example.cryptocurrencieslist.retrofit.CryptoCompareApi
import com.example.cryptocurrencieslist.retrofit.CurrencyApi
import com.example.cryptocurrencieslist.room.CurrenciesDB
import com.example.cryptocurrencieslist.utils.convertTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val adapter = CurrencyAdapter()
    private lateinit var db: CurrenciesDB
    private lateinit var binding: ActivityMainBinding
    private lateinit var currencyApi: CurrencyApi
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CurrenciesDB.getInstance(this)

        adapter.clearCurrencyList()
        setApiSettings()
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        binding.currenciesRv.adapter = adapter
        binding.currenciesRv.layoutManager = LinearLayoutManager(this@MainActivity)

        handler.post(object : Runnable {
            override fun run() {
                if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                    setInfo()
                    handler.postDelayed(this, 3000)
                    adapter.updateData()
                }
            }
        })

        if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
            setInfo()
            setCurrenciesList()
        }


        if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
            setInfo()
        }
        Thread.sleep(500)

        setCurrenciesList()
//        binding.updateButton.setOnClickListener {
//            if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
//                setInfo()
//                Thread.sleep(500)
//                adapter.updateData()
//            }
//        }
    }

    private fun setCurrenciesList() {

        CoroutineScope(Dispatchers.Main).launch {
            db.getDAO().getPriceInfo().collect { currencies ->
                adapter.clearCurrencyList()
                currencies.forEach { currency ->
                    adapter.addCurrency(Currency(CryptoCompareApi.DEFAULT_URL + currency.IMAGEURL,
                      currency.FROMSYMBOL, currency.PRICE, convertTime(currency.LASTUPDATE)
                    ))
                }
            }
        }
    }

    private fun setInfo() {
        CoroutineScope(Dispatchers.IO).launch {
//            db.getDAO().cleanInfo()
//            db.getDAO().clearPrice()
            for (i in 0 until currencyApi.getTopCurrencyList().Data.size - 1) {
                try {
                    val currency = currencyApi.getTopCurrencyList().Data[i]
                    db.getDAO().insertCurrency(currency.RAW.USD)
                    db.getDAO().insertCoinInfo(currency.CoinInfo)
                }
                catch (e: NullPointerException) {
                    continue
                }
            }
        }
    }
    private fun setApiSettings() {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://min-api.cryptocompare.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyApi = retrofit.create(CurrencyApi::class.java)
    }
}