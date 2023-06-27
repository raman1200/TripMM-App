package com.raman.verma.tripmmapp.DataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_details")
class TransactionData(
    @ColumnInfo val tripName:String,
    @ColumnInfo val description:String,
    @ColumnInfo val timeStamp:String,
    @ColumnInfo val paidBy:String,
    @ColumnInfo val amount:String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}