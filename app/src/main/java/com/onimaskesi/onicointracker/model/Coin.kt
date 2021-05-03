package com.onimaskesi.onicointracker.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["id"], unique = true, name = "index1")])
data class Coin(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "coinIndex")
        val coinIndex : Int,

        @ColumnInfo(name = "id")
        @SerializedName("id")
        val id : Long,

        @ColumnInfo(name = "name")
        @SerializedName("name")
        val name : String?,

        @ColumnInfo(name = "symbol")
        @SerializedName("symbol")
        val sym : String?,

        @Embedded
        @SerializedName("quote")
        val quote : Quote?,

        @ColumnInfo(name = "isFav")
        var isFavorite : Int?
)