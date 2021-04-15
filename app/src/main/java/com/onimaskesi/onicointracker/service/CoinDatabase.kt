package com.onimaskesi.onicointracker.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.USD

@Database(entities = arrayOf(Coin::class), version = 3)
abstract class CoinDatabase : RoomDatabase() {

    abstract fun coinDao() : CoinDao

    companion object {

        @Volatile private var instance : CoinDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context : Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CoinDatabase :: class.java,
                "roomDatabase"
            ).build()

    }



}