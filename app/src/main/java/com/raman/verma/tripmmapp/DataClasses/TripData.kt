package com.raman.verma.tripmmapp.DataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_details")
class TripData() {

    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    @ColumnInfo var tripName: String = ""
    @ColumnInfo var startDate:String = ""
    @ColumnInfo var endDate:String = ""
    @ColumnInfo var status:String = "ongoing"
    @ColumnInfo var members :Int = 0
    @ColumnInfo var m1 :String? = null
    @ColumnInfo var m2 :String? = null
    @ColumnInfo var m3 :String? = null
    @ColumnInfo var m4 :String? = null
    @ColumnInfo var m5 :String? = null
    @ColumnInfo var m6 :String? = null
    @ColumnInfo var m7 :String? = null
    @ColumnInfo var m8 :String? = null
    @ColumnInfo var m9 :String? = null
    @ColumnInfo var m10 :String? = null


}