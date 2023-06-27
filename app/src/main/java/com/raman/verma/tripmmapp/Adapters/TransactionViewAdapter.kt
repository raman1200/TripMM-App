package com.raman.verma.tripmmapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raman.verma.tripmmapp.DataClasses.TransactionData
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.databinding.TransactionItemViewBinding

class TransactionViewAdapter(val context: Context) : RecyclerView.Adapter<TransactionViewAdapter.TransactionViewHolder>() {

    private val list = ArrayList<TransactionData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item_view, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val data = list[position]
        holder.binding.description.text = data.description
        holder.binding.timeStamp.text = data.timeStamp
        holder.binding.paidBy.text = data.paidBy
        holder.binding.amount.text = "Rs.${data.amount}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getTransactionData(pos:Int):TransactionData {
        return list[pos]
    }

    inner class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding:TransactionItemViewBinding
        init {
            binding = TransactionItemViewBinding.bind(itemView)
        }
    }
    fun updateList(it: List<TransactionData>) {
        list.clear()
        list.addAll(it)
        notifyDataSetChanged()
    }
}