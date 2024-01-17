package com.example.cryptocurrencieslist.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocurrencieslist.adapter.Currency
import com.example.cryptocurrencieslist.retrofit.data.CoinInfo
import com.example.cryptocurrencieslist.retrofit.data.RAW
import com.example.cryptocurrencieslist.retrofit.data.USD
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrenciesDAO {
    @Query("SELECT * FROM price ORDER BY LASTUPDATE DESC")
    fun getPriceInfo(): List<USD>

    @Query("SELECT FROMSYMBOL FROM price ORDER BY LASTUPDATE DESC")
    fun getCoinName(): List<String>

    @Query("SELECT PRICE FROM price ORDER BY LASTUPDATE DESC")
    fun getPrice(): List<Double>

    @Query("SELECT LASTUPDATE FROM price ORDER BY LASTUPDATE DESC")
    fun getLastUpdate(): List<Long>

    @Query("SELECT IMAGEURL FROM price ORDER BY LASTUPDATE DESC")
    fun getImage(): List<String>

    @Query("SELECT COUNT(*) FROM price")
    fun getInfoSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(usd: USD)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinInfo(coinInfo: CoinInfo)
}