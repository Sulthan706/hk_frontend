package com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListBranchBinding
import com.hkapps.hygienekleen.features.features_management.complaint.model.listBranch.Data

class ListBranchComplaintManagementAdapter(
    private var context: Context,
    var listBranch: ArrayList<Data>
    ): RecyclerView.Adapter<ListBranchComplaintManagementAdapter.ViewHolder>() {

    private lateinit var branchCallBack: ListBranchCallBack

    inner class ViewHolder(val binding: ListBranchBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getBranch = listBranch[adapterPosition]
            branchCallBack.onClickBranch(getBranch.branchCode)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListBranchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listBranch[position]

        holder.binding.tvNameListBranch.text = response.branchName
        holder.binding.tvBranchCodeListBranch.text = response.branchCode
    }

    override fun getItemCount(): Int {
        return listBranch.size
    }

    fun setListener(listBranchCallBack: ListBranchCallBack) {
        this.branchCallBack = listBranchCallBack
    }

    interface ListBranchCallBack {
        fun onClickBranch(brancCode: String)
    }
}