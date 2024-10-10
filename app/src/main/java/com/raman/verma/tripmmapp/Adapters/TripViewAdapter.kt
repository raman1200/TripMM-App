package com.raman.verma.tripmmapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raman.verma.tripmmapp.DataClasses.TripData
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.databinding.TripItemViewBinding

class TripViewAdapter(private val context: Context, private val deleteClickIcon: DeleteIconClick, private val itemClick:ItemClick): RecyclerView.Adapter<TripViewAdapter.TripViewHolder>() {

    private val list:ArrayList<TripData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.trip_item_view, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val data = list[position]
        holder.binding.tripName.text = data.tripName
        holder.binding.startDate.text = "Started : ${data.startDate}"
        if(data.endDate.isNotEmpty()) {
            holder.binding.endDate.visibility = View.VISIBLE
            holder.binding.endDate.text = "Ended : ${data.endDate}"
        }
        holder.binding.members.text = "Members : ${data.members}"
        holder.binding.status.text = "Status : ${data.status}"
        holder.binding.delete.setOnClickListener {
            deleteClickIcon.onDeleteIconClick(data)
        }
        holder.itemView.setOnClickListener {
            itemClick.onItemClick(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding:TripItemViewBinding
        init {
            binding = TripItemViewBinding.bind(itemView)
        }
    }
    interface DeleteIconClick {
        fun onDeleteIconClick(data:TripData)
    }
    interface ItemClick {
        fun onItemClick(data:TripData)
    }
    fun updateList(it: List<TripData>) {
        list.clear()
        list.addAll(it)
        notifyDataSetChanged()
    }
}