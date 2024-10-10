package com.raman.verma.tripmmapp.Activities


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raman.verma.tripmmapp.Adapters.TransactionViewAdapter
import com.raman.verma.tripmmapp.Constants
import com.raman.verma.tripmmapp.Constants.Companion.COMPLETED
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.Fragments.TransactionAddFragment
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.Utils.showDeleteConfirmationDialog
import com.raman.verma.tripmmapp.ViewModels.MyViewModel
import com.raman.verma.tripmmapp.databinding.ActivityMainBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var sharedPref :SharedPreferences
    lateinit var transactionFragment: TransactionAddFragment
    lateinit var adapter:TransactionViewAdapter
    lateinit var viewModel: MyViewModel
    lateinit var tripData:TripData
    lateinit var callBackMethod:ItemTouchHelper.SimpleCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        setActionBar()
        init()
        clickListener()
        setSwipeAction()
        setUpAdapter()


    }


    private fun setSwipeAction() {
        callBackMethod = object : ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (tripData.status == COMPLETED) {
                    // Disable swiping for completed trips
                    makeMovementFlags(0, 0)
                } else {
                    // Enable swiping for other trips
                    val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    makeMovementFlags(0, swipeFlags)
                }
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT-> {
                        showDeleteConfirmationDialog(this@MainActivity, "Delete Transaction", "Are you sure you want to delete this transaction?",
                            onConfirm = {
                                viewModel.deleteTransaction(adapter.getTransactionData(viewHolder.layoutPosition))
                                Toast.makeText(this@MainActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                            },
                            onCancel = {
                                binding.recyclerView.adapter?.notifyItemChanged(viewHolder.layoutPosition)
                            })
                    }
                    ItemTouchHelper.RIGHT -> {
                        val bundle = Bundle()
                        bundle.putParcelable("data", adapter.getTransactionData(viewHolder.layoutPosition))
                        openBottomSheetWithArgs(bundle)

                    }
                }

            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Swipe left to delete
                    if (dX < 0) {
                        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeLeftLabel("Delete")
                            .setSwipeLeftLabelColor(resources.getColor(R.color.white))
                            .addSwipeLeftActionIcon(R.drawable.delete)
                            .setSwipeLeftActionIconTint(resources.getColor(R.color.white))
                            .addSwipeLeftBackgroundColor(resources.getColor(R.color.red))
                            .create()
                            .decorate()
                    }
                    // Swipe right to update
                    else if (dX > 0) {
                        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightLabel("Update")
                            .setSwipeRightLabelColor(resources.getColor(R.color.white))
                            .addSwipeRightActionIcon(R.drawable.update)
                            .setSwipeRightActionIconTint(resources.getColor(R.color.white))
                            .addSwipeRightBackgroundColor(resources.getColor(R.color.green))
                            .create()
                            .decorate()
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    fun observeData() {

        viewModel.allTransactionData.observe(this){
            it?.let{
                var amount= 0.0
                val list = ArrayList<TransactionData>()
                for(l in it){
                    if(l.tripName.equals(tripData.tripName)){
                        list.add(l)
                        amount += Integer.parseInt(l.amount)
                    }
                }
                if(list.isEmpty()){
                    binding.recyclerView.visibility = View.GONE
                    binding.noTransaction.visibility = View.VISIBLE
                }
                else{
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noTransaction.visibility = View.GONE
                }
                adapter.updateList(list)
                binding.recyclerView.scrollToPosition(list.size-1)
                binding.transaction.text = list.size.toString()
                binding.amount.text = "Rs.${amount}"


            }
        }
    }
    private fun setUpAdapter() {
        val llm = LinearLayoutManager(this)
        llm.reverseLayout = true
        llm.stackFromEnd = true
        binding.recyclerView.layoutManager = llm
        binding.recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(callBackMethod)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }
    private fun clickListener() {
        binding.addTransaction.setOnClickListener {
            if(tripData.status== COMPLETED){
                Toast.makeText(this, "This trip has ended. No further changes will occur.", Toast.LENGTH_SHORT).show()
            }else {
                openBottomSheet()
            }
        }
        binding.information.setOnClickListener {
            goToInformationActivity()
        }
        binding.clearAllBtn.setOnClickListener {
            if(tripData.status== COMPLETED){
                Toast.makeText(this, "This trip has ended. No further changes will occur.", Toast.LENGTH_SHORT).show()
            }else {
                showDeleteConfirmationDialog(this, "Delete All Transactions", "Are you sure you want to delete all transactions?", onConfirm = {
                    viewModel.deleteAllTransaction()
                    Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                }, onCancel = {})
            }
        }

    }

    private fun goToInformationActivity() {
        val intent = Intent(this, InformationActivity::class.java)
        startActivity(intent)

    }

    private fun openBottomSheet() {
        transactionFragment = TransactionAddFragment()
        transactionFragment.show(supportFragmentManager, transactionFragment.tag)
    }
    private fun openBottomSheetWithArgs(bundle:Bundle) {
        transactionFragment = TransactionAddFragment()
        transactionFragment.arguments = bundle
        transactionFragment.show(supportFragmentManager, transactionFragment.tag)
    }


    private fun getData() {
        tripData = TripData()
        sharedPref = getSharedPreferences(Constants.MYSHAREDPREF, MODE_PRIVATE)

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

        binding.tripName.paintFlags = binding.tripName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tripName.text = tripData.tripName

    }

    private fun setActionBar(){
        setSupportActionBar(binding.toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }


    private fun init() {
        adapter = TransactionViewAdapter(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(MyViewModel::class.java)
        observeData()


    }
}