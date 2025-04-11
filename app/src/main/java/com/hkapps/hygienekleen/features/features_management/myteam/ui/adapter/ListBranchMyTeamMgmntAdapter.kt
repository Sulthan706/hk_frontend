package com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListBranchBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.Data

class ListBranchMyTeamMgmntAdapter(
    private val context: Context,
    var listBranch: ArrayList<Data>
): RecyclerView.Adapter<ListBranchMyTeamMgmntAdapter.ViewHolder>() {

    private lateinit var listBranchCallBack: ListBranchCallBack

    inner class ViewHolder(val binding: ListBranchBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getBranch = listBranch[adapterPosition]
            listBranchCallBack.onClickBranch(getBranch.branchCode)
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
        this.listBranchCallBack = listBranchCallBack
    }

    interface ListBranchCallBack {
        fun onClickBranch(branchCode: String)
    }
}