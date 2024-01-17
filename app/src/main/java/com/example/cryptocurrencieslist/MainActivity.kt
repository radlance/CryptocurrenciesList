package com.example.cryptocurrencieslist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
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
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val adapter = CurrencyAdapter()
    private lateinit var db: CurrenciesDB
    private lateinit var binding: ActivityMainBinding
    private lateinit var currencyApi: CurrencyApi

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
        if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
            setInfo()
        }
        Thread.sleep(500)
        //TODO найти актуальные методы вызова метода без интернета и запуска setCurrenciesList после setInfo
        setCurrenciesList()
//        updateInfo()
    }

    private fun setCurrenciesList() {

        binding.currenciesRv.adapter = adapter
        binding.currenciesRv.layoutManager = LinearLayoutManager(this@MainActivity)
        CoroutineScope(Dispatchers.Main).launch {
            val currencyCount = withContext(Dispatchers.IO) {
                db.getDAO().getInfoSize()
            }
            for (i in 0 until currencyCount) {

                adapter.addCurrency(
                    Currency(
                        CryptoCompareApi.DEFAULT_URL + withContext(Dispatchers.IO) { db.getDAO().getImage()[i] },
                        withContext(Dispatchers.IO) { db.getDAO().getCoinName()[i] },
                        withContext(Dispatchers.IO) { db.getDAO().getPrice()[i] },
                        convertTime(withContext(Dispatchers.IO) { db.getDAO().getLastUpdate()[i] })
                    )
                )
            }
        }
//            adapter.clearCurrencyList()
//            for (currency in it) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    adapter.addCurrency(Currency(currency.Id,
//                        currency.Name, db.getDAO().getPrice(),
//                        convertTime(db.getDAO().getLastUpdate()))
//                    )
//                }
//            }
//            adapter.addCurrency(Currency("/media/44082045/sui.png", "BTC", 1.1, "11:00:00"))
//        binding.currenciesRv.adapter = adapter
//        binding.currenciesRv.layoutManager = LinearLayoutManager(this@MainActivity)
//        CoroutineScope(Dispatchers.IO).launch {
//            val currencyResponse = currencyApi.getTopCurrencyList().Data
//            runOnUiThread {
//                for (i in 0 until currencyResponse.size - 1) {
//                    val currencyImage =
//                        CryptoCompareApi.DEFAULT_URL + currencyResponse[i].CoinInfo.ImageUrl
//                    adapter.addCurrency(
//                        Currency(
//                            currencyImage,
//                            currencyResponse[i].CoinInfo.Name, currencyResponse[i].RAW.USD.PRICE,
//                            convertTime(currencyResponse[i].RAW.USD.LASTUPDATE)
//                        )
//                    )
//                }
//            }
//        }
    }

    private fun setInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val currencyResponse = currencyApi.getTopCurrencyList().Data
            for (i in 0 until currencyResponse.size - 1) {
                try {
                    db.getDAO().insertCurrency(currencyResponse[i].RAW.USD)
                    db.getDAO().insertCoinInfo(currencyResponse[i].CoinInfo)
                }
                catch (e: NullPointerException) {
                    continue
                }
            }
        }
    }

    //TODO set data  from database

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