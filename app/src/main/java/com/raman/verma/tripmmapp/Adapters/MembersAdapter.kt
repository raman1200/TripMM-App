package com.raman.verma.tripmmapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raman.verma.tripmmapp.Activities.MembersData
import com.raman.verma.tripmmapp.R
import com.raman.verma.tripmmapp.databinding.InformationItemViewBinding
import com.raman.verma.tripmmapp.databinding.MemberDetailsBinding

class MembersAdapter(val context: Context, val list:ArrayList<MembersData>) : RecyclerView.Adapter<MembersAdapter.MembersViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.information_item_view, parent, false)
        return MembersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val members=  list[position]
        holder.binding.memberName.text = members.name
        holder.binding.amount.text = "Rs.${members.amount}"
    }

    inner class MembersViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val binding:InformationItemViewBinding
        init {
            binding = InformationItemViewBinding.bind(itemView)
        }
    }

}
