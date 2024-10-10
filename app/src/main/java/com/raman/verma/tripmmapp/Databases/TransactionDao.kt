package com.raman.verma.tripmmapp.Databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.raman.verma.tripmmapp.DataClasses.TransactionData

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionData: TransactionData)

    @Delete
    suspend fun delete(transactionData: TransactionData)

    @Update
    suspend fun update(transactionData: TransactionData)

    @Query("DELETE FROM transaction_details")
    suspend fun deleteAll()

    @Query("Select * from transaction_details")
    fun getTransactionData() : LiveData<List<TransactionData>>

}