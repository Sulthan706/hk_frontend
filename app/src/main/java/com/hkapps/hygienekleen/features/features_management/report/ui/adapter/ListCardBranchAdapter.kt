package com.hkapps.hygienekleen.features.features_management.report.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListBranchProjectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.cardlistbranch.DataCardBranch

class ListCardBranchAdapter (private val context: Context,
var listBranchMgmnt: ArrayList<DataCardBranch>
):
    RecyclerView.Adapter<ListCardBranchAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: ItemListBranchProjectBinding):
                RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBranchProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listBranchMgmnt[position]

            holder.binding.tvBranchName.text = item.branchName

    }

    override fun getItemCount(): Int {
        return listBranchMgmnt.size
    }
}