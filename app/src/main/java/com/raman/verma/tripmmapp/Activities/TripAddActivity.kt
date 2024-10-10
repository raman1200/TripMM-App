package com.raman.verma.tripmmapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.ViewModels.MyViewModel
import com.raman.verma.tripmmapp.databinding.ActivityTripAddBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TripAddActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTripAddBinding
    private lateinit var data: TripData
    private lateinit var memberList:ArrayList<String>
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        init()
        clickListeners()
    }
    private fun init() {
        data = TripData()
        memberList = ArrayList()
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[MyViewModel::class.java]
    }

    private fun setActionBar(){
        setSupportActionBar(binding.toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }
    private fun clearAll() {
        binding.addLayout.removeAllViews()
        binding.tripName.setText("")
        binding.tripName.clearFocus()
    }
    private fun clickListeners() {
        binding.addMember.setOnClickListener {
            addView()
        }
        binding.addBtn.setOnClickListener {
            checkAll()
        }
    }
    private fun setMembers() {
        val i = binding.addLayout.childCount
        data.members = i
        if(i==0) return
        data.m1 = memberList[0]
        if(i==1) return
        data.m2 = memberList[1]
        if(i==2) return
        data.m3 = memberList[2]
        if(i==3) return
        data.m4 = memberList[3]
        if(i==4) return
        data.m5 = memberList[4]
        if(i==5) return
        data.m6 = memberList[5]
        if(i==6) return
        data.m7 = memberList[6]
        if(i==7) return
        data.m8 = memberList[7]
        if(i==8) return
        data.m9 = memberList[8]
        if(i==9) return
        data.m10 = memberList[9]

    }
    private fun setDateAndTIme() {
        val date = Date()
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a")
        data.startDate = sdf.format(date)

    }
    private fun checkAll() {
        val tripName = binding.tripName.text.toString()
        if(tripName.isEmpty()){
            binding.tripName.error = "Please Enter Trip Name"
        }
        else if(binding.addLayout.childCount == 0){
            Toast.makeText(this, "Please Add Atleast 1 Member", Toast.LENGTH_SHORT).show()
        }
        else {
            data.tripName = tripName
            checkMemberDetails()
            setDateAndTIme()
            setMembers()

            if(memberList.isNotEmpty()){
                viewModel.addTrip(data)
                clearAll()
                Toast.makeText(this, "Trip Added Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkMemberDetails() {
        val count = binding.addLayout.childCount
        memberList.clear()
        for(i in 0..count){
            val view = binding.addLayout.getChildAt(i) ?: return
            val memberName = view.findViewById<EditText>(R.id.memberName)!!
            val name = memberName.text.toString()
            if(name.isEmpty()){
                memberName.error = "Please Enter Member Name"
                return
            }
            memberList.add(name)
        }

    }

    private fun addView() {
        if(binding.addLayout.childCount>10){
            Toast.makeText(this, "Maximum limit reached", Toast.LENGTH_SHORT).show()
            return
        }
        val memberView = layoutInflater.inflate(R.layout.member_details, null, false)
        val closeIcon = memberView.findViewById<ImageView>(R.id.close_icon)

        closeIcon.setOnClickListener {
            binding.addLayout.removeView(memberView)
        }

        binding.addLayout.addView(memberView)
    }

}