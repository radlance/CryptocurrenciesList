package com.example.cryptocurrencieslist.retrofit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "price")
data class USD(
    var TYPE: String,
    var MARKET: String,
    @PrimaryKey(autoGenerate = false)
    var FROMSYMBOL: String,
    var TOSYMBOL: String,
    var FLAGS: String,
    var LASTMARKET: String,
    var MEDIAN: Double,
    var TOPTIERVOLUME24HOUR: Double,
    var TOPTIERVOLUME24HOURTO: Double,
    var LASTTRADEID: String,
    var PRICE: Double,
    var LASTUPDATE: Long,
    var LASTVOLUME: Double,
    var LASTVOLUMETO: Double,
    var VOLUMEHOUR: Double,
    var VOLUMEHOURTO: Double,
    var OPENHOUR: Double,
    var HIGHHOUR: Double,
    var LOWHOUR: Double,
    var VOLUMEDAY: Double,
    var VOLUMEDAYTO: Double,
    var OPENDAY: Double,
    var HIGHDAY: Double,
    var LOWDAY: Double,
    var VOLUME24HOUR: Double,
    var VOLUME24HOURTO: Double,
    var OPEN24HOUR: Double,
    var HIGH24HOUR: Double,
    var LOW24HOUR: Double,
    var CHANGE24HOUR: Double,
    var CHANGEPCT24HOUR: Double,
    var CHANGEDAY: Double,
    var CHANGEPCTDAY: Double,
    var CHANGEHOUR: Double,
    var CHANGEPCTHOUR: Double,
    var CONVERSIONTYPE: String,
    var CONVERSIONSYMBOL: String,
    var CONVERSIONLASTUPDATE: Long,
    var SUPPLY: Double,
    var MKTCAP: Double,
    var MKTCAPPENALTY: Double,
    var CIRCULATINGSUPPLY: Double,
    var CIRCULATINGSUPPLYMKTCAP: Double,
    var TOTALVOLUME24H: Double,
    var TOTALVOLUME24HTO: Double,
    var TOTALTOPTIERVOLUME24H: Double,
    var TOTALTOPTIERVOLUME24HTO: Double,
    var IMAGEURL: String
)