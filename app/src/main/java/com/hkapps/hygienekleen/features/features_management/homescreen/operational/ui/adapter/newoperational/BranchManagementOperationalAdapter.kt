package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemOperationalBranchBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational.ContentBranchOperational

class BranchManagementOperationalAdapter(
    private var context: Context,
    var listBranchOperational: ArrayList<ContentBranchOperational>
) : RecyclerView.Adapter<BranchManagementOperationalAdapter.ViewHolder>() {

    private lateinit var listBranchManagementCallback: ListBranchManagementCallback



    inner class ViewHolder(val binding: ItemOperationalBranchBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
            init {
                itemView.setOnClickListener(this)
            }

        override fun onClick(v: View?) {
            val response = listBranchOperational[adapterPosition]
            listBranchManagementCallback.onClickManagement(response.idCabang, response.branchName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOperationalBranchBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listBranchOperational[position]
        holder.binding.tvBranchName.text = if (item.branchName.isNullOrEmpty()) "-" else item.branchName
        holder.binding.tvBranch.text = if (item.branchCode.isNullOrEmpty()) "-" else item.branchCode
        holder.binding.tvTotalManpower.text = if (item.totalActiveUser.toString().isNullOrEmpty()) "-" else "${item.totalActiveUser} MP"
    }

    override fun getItemCount(): Int {
       return listBranchOperational.size
    }


    fun setListener(listBranchManagementCallback: ListBranchManagementCallback){
        this.listBranchManagementCallback = listBranchManagementCallback
    }



    interface ListBranchManagementCallback {
        fun onClickManagement(idCabang: Int, branchName: String)
    }

}