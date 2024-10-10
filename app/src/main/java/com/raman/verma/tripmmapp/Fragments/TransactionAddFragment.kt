package com.raman.verma.tripmmapp.Fragments

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raman.verma.tripmmapp.Activities.MainActivity
import com.raman.verma.tripmmapp.Constants
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.ViewModels.MyViewModel
import com.raman.verma.tripmmapp.databinding.FragmentTransactionAddBinding
import kotlinx.coroutines.newFixedThreadPoolContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAddFragment : BottomSheetDialogFragment() {
    lateinit var binding:FragmentTransactionAddBinding
    lateinit var viewModel: MyViewModel
    lateinit var sharedPref:SharedPreferences
    lateinit var tripData: TripData
    private lateinit var paidBy: String
    var transactionData: TransactionData? = null
    private var timeStamp = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionAddBinding.inflate(layoutInflater, container, false)

        arguments?.let {
            transactionData = arguments?.getParcelable("data")!!
            binding.apply {
                description.setText(transactionData!!.description)
                amount.setText(transactionData!!.amount)
                heading.setText("Update Transaction")
                addBtn.setText("Update")

            }
        }
        init()
        setDateAndTIme()
        getData()
        setSpinner()
        clickListener()

        return binding.root
    }

    private fun clickListener() {
        binding.addBtn.setOnClickListener {
            checkAll()
        }
    }

    private fun checkAll() {
        val description = binding.description.text.toString()
        paidBy = binding.dropdown.selectedItem.toString()
        val amount = binding.amount.text.toString()
        if(description.isEmpty()){
            binding.description.error = "Please Enter the Description"
            return
        }
        if(paidBy.equals("Select PaidBy Name")){
            Toast.makeText(requireActivity(), "Please Select the Member Name", Toast.LENGTH_SHORT).show()
            return
        }
        if(amount.isEmpty()){
            binding.amount.error = "Please Enter the Amount"
            return
        }
        if(transactionData!=null){
            transactionData!!.amount = amount
            transactionData!!.description = description
            transactionData!!.paidBy = paidBy
            transactionData!!.timeStamp = timeStamp
            viewModel.updateTransaction(transactionData!!)
            Toast.makeText(requireActivity(), "Transaction Updated Successfully", Toast.LENGTH_SHORT).show()
        }
        else{
            transactionData = TransactionData(tripData.tripName, description, timeStamp, paidBy, amount)
            viewModel.addTransaction(transactionData!!)
            Toast.makeText(requireActivity(), "Transaction Added Successfully", Toast.LENGTH_SHORT).show()
        }
        clearAll()

    }
    private fun clearAll() {
        binding.description.setText("")
        binding.amount.setText("")
        binding.dropdown.setSelection(0)
        binding.description.requestFocus()
    }

    private fun setDateAndTIme() {
        val date = Date()
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a")
        timeStamp = sdf.format(date)
    }

    private fun init() {
        tripData = TripData()
        sharedPref = requireActivity().getSharedPreferences(Constants.MYSHAREDPREF, AppCompatActivity.MODE_PRIVATE)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MyViewModel::class.java)
    }
    private fun getData() {
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
    }
    private fun setSpinner() {
        val list = ArrayList<String>()
        list.add("Select PaidBy Name")
        if(tripData.m1!= null) list.add(tripData.m1!!)
        if(tripData.m2!= null) list.add(tripData.m2!!)
        if(tripData.m3!= null) list.add(tripData.m3!!)
        if(tripData.m4!= null) list.add(tripData.m4!!)
        if(tripData.m5!= null) list.add(tripData.m5!!)
        if(tripData.m6!= null) list.add(tripData.m6!!)
        if(tripData.m7!= null) list.add(tripData.m7!!)
        if(tripData.m8!= null) list.add(tripData.m8!!)
        if(tripData.m9!= null) list.add(tripData.m9!!)
        if(tripData.m10!= null) list.add(tripData.m10!!)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, list)
        binding.dropdown.adapter = adapter

        list.forEach{
            if(transactionData!=null){
                if(it == transactionData!!.paidBy){
                    binding.dropdown.setSelection(list.indexOf(it))
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.observeData()
        }

    }
}