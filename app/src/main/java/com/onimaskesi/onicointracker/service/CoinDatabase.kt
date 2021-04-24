package com.onimaskesi.onicointracker.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.FavCoin
import com.onimaskesi.onicointracker.model.USD

@Database(entities = arrayOf(Coin::class, FavCoin::class), version = 7)
abstract class CoinDatabase : RoomDatabase() {

    abstract fun coinDao() : CoinDao
    abstract fun favCoinDao() : FavCoinDao

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
            ).addMigrations(MIGRATION_3_7).build()

        val MIGRATION_3_7: Migration = object : Migration(3, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `FavCoin` (`id` INTEGER PRIMARY KEY NOT NULL, `sym` TEXT NOT NULL)")
            }
        }

    }



}