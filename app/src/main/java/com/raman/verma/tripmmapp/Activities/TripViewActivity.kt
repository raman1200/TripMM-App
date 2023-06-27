package com.raman.verma.tripmmapp.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raman.verma.tripmmapp.Adapters.TripViewAdapter
import com.raman.verma.tripmmapp.Constants
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.ViewModels.MyViewModel
import com.raman.verma.tripmmapp.databinding.ActivityTripViewBinding
import java.util.*

class TripViewActivity : AppCompatActivity(), TripViewAdapter.DeleteIconClick,
    TripViewAdapter.ItemClick {
    lateinit var binding: ActivityTripViewBinding
    lateinit var viewModel: MyViewModel
    lateinit var adapter: TripViewAdapter
    lateinit var sharedPref:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripViewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        init()
        setUpAdapter()
        clickListeners()

    }


    private fun setUpAdapter() {
        val llm = LinearLayoutManager(this)
        llm.reverseLayout = true
        llm.stackFromEnd = true
        binding.tripRecyclerView.layoutManager = llm
        binding.tripRecyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.allTripData.observe(this){
            it?.let {
                if (it.isNotEmpty()) {
                    binding.tripRecyclerView.visibility = View.VISIBLE
                    binding.noTrip.visibility = View.GONE
                } else {
                    binding.tripRecyclerView.visibility = View.GONE
                    binding.noTrip.visibility = View.VISIBLE
                }
                adapter.updateList(it)
                binding.tripCount.text = it.size.toString()
            }
        }
    }
    private fun init() {
        adapter = TripViewAdapter(this, this, this)
        sharedPref = getSharedPreferences(Constants.MYSHAREDPREF, MODE_PRIVATE)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MyViewModel::class.java)
        observeData()
    }
    private fun clickListeners() {
        binding.addBtn.setOnClickListener{
            startActivity(Intent(this, TripAddActivity::class.java))
        }
    }

    override fun onDeleteIconClick(data: TripData) {
        viewModel.deleteTrip(data)
    }

    override fun onItemClick(data: TripData) {
        val intent = Intent(this, MainActivity::class.java)
        setEditor(data)
        startActivity(intent)
//        Toast.makeText(this, "Item Clicked", Toast.LENGTH_SHORT).show()
    }
    private fun setEditor(data:TripData) {
        val editor = sharedPref.edit()
        editor.putString(Constants.TRIPNAME, data.tripName)
        editor.putString(Constants.TIMESTAMP, data.timeStamp)
        editor.putInt(Constants.MEMBERS, data.members)
        editor.putString(Constants.M1, data.m1)
        editor.putString(Constants.M2, data.m2)
        editor.putString(Constants.M3, data.m3)
        editor.putString(Constants.M4, data.m4)
        editor.putString(Constants.M5, data.m5)
        editor.putString(Constants.M6, data.m6)
        editor.putString(Constants.M7, data.m7)
        editor.putString(Constants.M8, data.m8)
        editor.putString(Constants.M9, data.m9)
        editor.putString(Constants.M10, data.m10)
        editor.apply()
    }
}