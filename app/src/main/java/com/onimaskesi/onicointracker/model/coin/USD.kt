package com.onimaskesi.onicointracker.model.coin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class USD(

        @SerializedName("price")
        val price : String?,

        @SerializedName("volume_24h")
        val volume : String?,

        @SerializedName("percent_change_1h")
        val oneHourPercent : String?,

        @SerializedName("percent_change_24h")
        val oneDayPercent : String?,

        @SerializedName("percent_change_7d")
        val sevenDayPercent : String?,

        @SerializedName("percent_change_30d")
        val thirtyDayPercent : String?,

        @SerializedName("percent_change_60d")
        val sixtyDayPercent : String?,

        @SerializedName("percent_change_90d")
        val ninetyDayPercent : String?

)