package com.example.cryptocurrencieslist.retrofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("info")
data class CoinInfo(
    @PrimaryKey(autoGenerate = false) val Id: String,
    val Name: String,
    val FullName: String,
    val ImageUrl: String
)
