package com.raman.verma.tripmmapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.Databases.AppDatabase
import com.raman.verma.tripmmapp.Repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {
    val allTripData: LiveData<List<TripData>>
    val allTransactionData :LiveData<List<TransactionData>>
    private val repository : Repository


    init {
        val database = AppDatabase.getDatabase(application)
        repository = Repository(database.getTransactionDao(), database.getTripDao())
        allTripData = repository.allTripData
        allTransactionData = repository.allTransactionData
    }
    fun deleteTrip(data: TripData) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTrip(data)
    }
    fun updateTrip(data: TripData) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTrip(data)
    }
    fun addTrip(data: TripData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTrip(data)
    }

    fun deleteTransaction(data: TransactionData) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTransaction(data)
    }
    fun updateTransaction(data: TransactionData) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTransaction(data)
    }
    fun addTransaction(data: TransactionData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTransaction(data)
    }

}