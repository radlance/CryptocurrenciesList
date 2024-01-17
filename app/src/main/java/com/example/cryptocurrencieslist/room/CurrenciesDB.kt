package com.example.cryptocurrencieslist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptocurrencieslist.adapter.Currency
import com.example.cryptocurrencieslist.retrofit.data.CoinInfo
import com.example.cryptocurrencieslist.retrofit.data.Data
import com.example.cryptocurrencieslist.retrofit.data.USD


@Database(entities = [USD::class, CoinInfo::class], version = 2)
abstract class CurrenciesDB : RoomDatabase() {

    abstract fun getDAO(): CurrenciesDAO

    companion object {
        fun getInstance(context: Context): CurrenciesDB {
            return Room.databaseBuilder(context.applicationContext,
                CurrenciesDB::class.java,
                "Currencies.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}