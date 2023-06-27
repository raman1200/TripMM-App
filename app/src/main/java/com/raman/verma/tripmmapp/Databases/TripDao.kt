package com.raman.verma.tripmmapp.Databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.raman.verma.tripmmapp.DataClasses.TripData

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: TripData)

    @Delete
    suspend fun delete(data: TripData)

    @Update
    suspend fun update(data: TripData)

    @Query("Select * from trip_details")
    fun getTripData():LiveData<List<TripData>>
}