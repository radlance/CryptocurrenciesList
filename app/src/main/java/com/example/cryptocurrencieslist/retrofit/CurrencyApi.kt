package com.example.cryptocurrencieslist.retrofit

import com.example.cryptocurrencieslist.retrofit.data.ResponseInfo
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyApi {
    @GET("/data/top/totalvolfull")
    suspend fun getTopCurrencyList(@Query("limit") limit: String = CryptoCompareApi.LIMIT,
                           @Query("tsym") tsym: String = CryptoCompareApi.TSYM,
                           @Query("api_key") apiKey: String = CryptoCompareApi.API_KEY): ResponseInfo
}