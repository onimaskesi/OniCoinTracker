package com.onimaskesi.onicointracker.service.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.model.favcoin.FavCoin

@Database(entities = arrayOf(Coin::class, FavCoin::class), version = 9)
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
            ).addMigrations(MIGRATION_3_8, MIGRATION_8_9).build()


        private val MIGRATION_3_8: Migration = object : Migration(3, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `FavCoin` (`id` INTEGER PRIMARY KEY NOT NULL, `sym` TEXT NOT NULL)")
                database.execSQL("ALTER TABLE Coin ADD COLUMN isFav INTEGER")
            }
        }

        private val MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //create new table
                database.execSQL("CREATE TABLE `NewCoinTable`(`coinIndex` INTEGER NOT NULL," +
                        " `id` INTEGER NOT NULL," +
                        " `name` TEXT, " +
                        "`symbol` TEXT," +
                        " `isFav` INTEGER," +
                        " `price` TEXT," +
                        " `volume` TEXT," +
                        " `oneHourPercent` TEXT," +
                        " `oneDayPercent` TEXT," +
                        " `sevenDayPercent` TEXT," +
                        " `thirtyDayPercent` TEXT," +
                        " `sixtyDayPercent` TEXT," +
                        " `ninetyDayPercent` TEXT," +
                        " PRIMARY KEY(`coinIndex`) )")

                //insert data from old table into new table
                database.execSQL("INSERT INTO NewCoinTable(id," +
                        " name," +
                        " symbol," +
                        " isFav," +
                        " price," +
                        " volume," +
                        " oneHourPercent," +
                        " oneDayPercent," +
                        " sevenDayPercent," +
                        " thirtyDayPercent," +
                        " sixtyDayPercent," +
                        " ninetyDayPercent)" +
                        " SELECT" +
                        " id, name, symbol," +
                        " isFav, price, volume," +
                        " oneHourPercent, oneDayPercent, sevenDayPercent," +
                        " thirtyDayPercent, sixtyDayPercent, ninetyDayPercent FROM Coin")

                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index1 ON NewCoinTable (id)")

                //drop old table
                database.execSQL("DROP TABLE Coin")

                //rename new table to the old table name
                database.execSQL("ALTER TABLE NewCoinTable RENAME TO Coin")

            }
        }

    }

}