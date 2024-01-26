package com.example.cryptocurrencieslist.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocurrencieslist.retrofit.data.CoinInfo
import com.example.cryptocurrencieslist.retrofit.data.USD
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrenciesDAO {
    @Query("SELECT * FROM price ORDER BY LASTUPDATE DESC, PRICE DESC")
    fun getPriceInfo(): Flow<List<USD>>

    @Query("SELECT FROMSYMBOL FROM price ORDER BY LASTUPDATE DESC, PRICE DESC")
    fun getCoinName(): Flow<List<String>>
    
    @Query("SELECT PRICE FROM price ORDER BY LASTUPDATE DESC, PRICE DESC")
    fun getPrice(): Flow<List<Double>>

    @Query("SELECT LASTUPDATE FROM price ORDER BY LASTUPDATE DESC, PRICE DESC")
    fun getLastUpdate(): Flow<List<Long>>

    @Query("SELECT IMAGEURL FROM price ORDER BY LASTUPDATE DESC, PRICE DESC")
    fun getImage(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM price")
    fun getInfoSize(): Flow<Int>

    @Query("SELECT * FROM price WHERE FROMSYMBOL = :name")
    suspend fun getInfoByName(name: String): USD

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(usd: USD)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinInfo(coinInfo: CoinInfo)

}