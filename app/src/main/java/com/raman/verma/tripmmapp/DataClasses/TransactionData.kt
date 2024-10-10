package com.raman.verma.tripmmapp.DataClasses

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "transaction_details")
@Parcelize
class TransactionData(
    @ColumnInfo val tripName:String,
    @ColumnInfo var description:String,
    @ColumnInfo var timeStamp:String,
    @ColumnInfo var paidBy:String,
    @ColumnInfo var amount:String,
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}