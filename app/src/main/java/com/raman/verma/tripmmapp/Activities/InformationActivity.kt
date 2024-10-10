package com.raman.verma.tripmmapp.Activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raman.verma.tripmmapp.Adapters.MembersAdapter
import com.raman.verma.tripmmapp.Constants
import com.raman.verma.tripmmapp.Constants.Companion.COMPLETED
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.Utils.showDeleteConfirmationDialog
import com.raman.verma.tripmmapp.ViewModels.MyViewModel
import com.raman.verma.tripmmapp.databinding.ActivityInformationBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.ceil

class InformationActivity : AppCompatActivity() {
    lateinit var binding:ActivityInformationBinding
    lateinit var viewModel: MyViewModel
    lateinit var sharedPref: SharedPreferences
    var tripName = ""
    lateinit var tripData:TripData
    lateinit var list:ArrayList<TransactionData>
    lateinit var map :HashMap<String, Double>
    var avg:Double=0.0
    private lateinit var mapList:ArrayList<MembersData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        init()
        getData()
        observeTransactionData()

        clickListeners()

    }

    private fun clickListeners() {
        binding.apply {
            endTrip.setOnClickListener {
                showDeleteConfirmationDialog(this@InformationActivity, "End Trip", "Are you sure you want to end this trip?", onConfirm = {
                    tripData.endDate = setDateAndTIme()
                    tripData.status = COMPLETED
                    viewModel.updateTrip(tripData)
                    sharedPref.edit().putString(Constants.STATUS, COMPLETED).apply()
                    Toast.makeText(this@InformationActivity, "Trip Ended Successfully", Toast.LENGTH_SHORT).show()
                },
                    onCancel = {

                    }
                )
            }
        }
    }
    private fun setDateAndTIme(): String {
        val date = Date()
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a")
        return sdf.format(date)
    }

    private fun init() {
        tripData = TripData()
        mapList = ArrayList()
        sharedPref = getSharedPreferences(Constants.MYSHAREDPREF, MODE_PRIVATE)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(MyViewModel::class.java)


    }

    private fun observeTransactionData() {
        viewModel.allTransactionData.observe(this){
            it?.let{
                var amount:Double = 0.0
                list = ArrayList()
                for(l in it){
                    if(l.tripName.equals(tripData.tripName)){
                        list.add(l)
                        amount += Integer.parseInt(l.amount)
                        map[l.paidBy] = map[l.paidBy]!!.plus(Integer.parseInt(l.amount))
                    }
                }
                binding.transaction.text = "Total Transaction : ${list.size}"
                binding.amount.text = "Total Amount Spend : Rs.${amount}/-"
                binding.average.text = "Average spend Amount is : ${ceil(amount/tripData.members)}/-"
                avg = ceil(amount/tripData.members)

                for(m in map){
                    mapList.add(MembersData(m.key, m.value))
                }
                setAdapter()
            }
        }
    }
    private fun setAdapter() {
        val adapter = MembersAdapter(this, mapList, avg)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)


    }

    private fun getData() {
        tripData.id = sharedPref.getInt(Constants.ID, -1)
        tripData.tripName = sharedPref.getString(Constants.TRIPNAME, null).toString()
        tripData.startDate = sharedPref.getString(Constants.STARTDATE ,null).toString()
        tripData.endDate = sharedPref.getString(Constants.ENDDATE ,null).toString()
        tripData.status = sharedPref.getString(Constants.STATUS ,null).toString()

        tripData.members = sharedPref.getInt(Constants.MEMBERS, -1)
        tripData.m1 = sharedPref.getString(Constants.M1, null)
        tripData.m2 = sharedPref.getString(Constants.M2, null)
        tripData.m3= sharedPref.getString(Constants.M3, null)
        tripData.m4 = sharedPref.getString(Constants.M4, null)
        tripData.m5 = sharedPref.getString(Constants.M5, null)
        tripData.m6= sharedPref.getString(Constants.M6, null)
        tripData.m7 = sharedPref.getString(Constants.M7, null)
        tripData.m8 = sharedPref.getString(Constants.M8, null)
        tripData.m9= sharedPref.getString(Constants.M9, null)
        tripData.m10= sharedPref.getString(Constants.M10, null)


        if(tripData.status == COMPLETED){
            binding.endTrip.visibility = View.GONE
        }

        setList()
        setData()
    }

    private fun setData() {
        binding.apply {
            name.text = "Trip Name : ${tripData.tripName}"
            startDate.text = "Trip Start Date : ${tripData.startDate}"
            status.text = "Trip Status : ${tripData.status}"
            if(tripData.status== COMPLETED) {
                endDate.visibility = View.VISIBLE
                endDate.text = "Trip End Date : ${tripData.endDate}"
            }
        }
    }

    private fun setList() {
        map = HashMap()
        if(tripData.m1!= null) map[tripData.m1!!] = 0.0
        if(tripData.m2!= null) map[tripData.m2!!] = 0.0
        if(tripData.m3!= null) map[tripData.m3!!] = 0.0
        if(tripData.m4!= null) map[tripData.m4!!] = 0.0
        if(tripData.m5!= null) map[tripData.m5!!] = 0.0
        if(tripData.m6!= null) map[tripData.m6!!] = 0.0
        if(tripData.m7!= null) map[tripData.m7!!] = 0.0
        if(tripData.m8!= null) map[tripData.m8!!] = 0.0
        if(tripData.m9!= null) map[tripData.m9!!] = 0.0
        if(tripData.m10!= null) map[tripData.m10!!] = 0.0

    }

    private fun setActionBar(){
        setSupportActionBar(binding.toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }


}
class MembersData(val name:String, val amount:Double){

}