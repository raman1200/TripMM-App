package com.raman.verma.tripmmapp.Activities

import android.R
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.GridLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.raman.verma.tripmmapp.Adapters.MembersAdapter
import com.raman.verma.tripmmapp.Constants
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.ViewModels.MyViewModel
import com.raman.verma.tripmmapp.databinding.ActivityInformationBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InformationActivity : AppCompatActivity() {
    lateinit var binding:ActivityInformationBinding
    lateinit var viewModel: MyViewModel
    lateinit var sharedPref: SharedPreferences
    var tripName = ""
    lateinit var tripData:TripData
    lateinit var list:ArrayList<TransactionData>
    lateinit var map :HashMap<String, Double>
    private lateinit var mapList:ArrayList<MembersData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        init()
        getData()
        observeTransactionData()
        setAdapter()

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
                binding.average.text = "Average spend Amount is : ${Math.ceil(amount/tripData.members)}/-"
                for(m in map){
                    mapList.add(MembersData(m.key, m.value))
                }
            }
        }
    }
    private fun setAdapter() {
        val adapter = MembersAdapter(this, mapList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

    }

    private fun getData() {

        tripData.tripName = sharedPref.getString(Constants.TRIPNAME, null).toString()
        tripData.timeStamp = sharedPref.getString(Constants.TIMESTAMP ,null).toString()
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
        setList()
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
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }


}
class MembersData(val name:String, val amount:Double){

}