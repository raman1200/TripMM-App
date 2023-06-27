package com.raman.verma.tripmmapp.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData

@Database(entities = [TripData::class, TransactionData::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {


    abstract fun getTransactionDao() : TransactionDao

    abstract fun getTripDao() : TripDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").build()
                INSTANCE = instance
                instance
            }

        }
    }
}