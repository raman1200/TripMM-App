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
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.Fragments.TransactionAddFragment
import com.raman.verma.tripmmapp.R
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
    var tripName =""
    lateinit var callBackMethod:ItemTouchHelper.SimpleCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        init()
        clickListener()
        getData()
        setSwipeAction()
        setUpAdapter()


    }

    private fun setSwipeAction() {
        callBackMethod = object : ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT-> {
                        viewModel.deleteTransaction(adapter.getTransactionData(viewHolder.layoutPosition))
                        Toast.makeText(this@MainActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete").setSwipeLeftLabelColor(resources.getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.delete).setSwipeLeftActionIconTint(resources.getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(resources.getColor(R.color.red)).create().decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }
    }

    private fun observeData() {

        viewModel.allTransactionData.observe(this){
            it?.let{
                var amount= 0.0
                val list = ArrayList<TransactionData>()
                for(l in it){
                    if(l.tripName.equals(tripName)){
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
            openBottomSheet()
        }
        binding.information.setOnClickListener {
            goToInformationActivity()
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

    private fun getData() {
        sharedPref = getSharedPreferences(Constants.MYSHAREDPREF, MODE_PRIVATE)
        tripName = sharedPref.getString(Constants.TRIPNAME, "Empty").toString()
        binding.tripName.paintFlags = binding.tripName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tripName.text = tripName

    }

    private fun setActionBar(){
        setSupportActionBar(binding.toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }


    private fun init() {
        adapter = TransactionViewAdapter(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(MyViewModel::class.java)
        observeData()
    }
}