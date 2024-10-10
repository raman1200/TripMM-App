package com.raman.verma.tripmmapp.Repositories

import androidx.lifecycle.LiveData
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.Databases.TransactionDao
import com.raman.verma.tripmmapp.Databases.TripDao

class Repository(private val transactionDao: TransactionDao, private val tripDao: TripDao) {

    val allTransactionData: LiveData<List<TransactionData>> = transactionDao.getTransactionData()

    suspend fun insertTransaction(data: TransactionData) {
        transactionDao.insert(data)
    }
    suspend fun deleteTransaction(data: TransactionData) {
        transactionDao.delete(data)
    }
    suspend fun updateTransaction(data: TransactionData) {
        transactionDao.update(data)
    }
    suspend fun deleteAllTransaction() {
        transactionDao.deleteAll()
    }


    val allTripData: LiveData<List<TripData>> = tripDao.getTripData()

    suspend fun insertTrip(data: TripData) {
        tripDao.insert(data)
    }
    suspend fun deleteTrip(data: TripData){
        tripDao.delete(data)
    }
    suspend fun updateTrip(data: TripData){
        tripDao.update(data)
    }

}