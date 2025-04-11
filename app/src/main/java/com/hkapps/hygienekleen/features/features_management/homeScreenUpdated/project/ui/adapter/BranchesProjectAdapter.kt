package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.BranchesProjectBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.Content

class BranchesProjectAdapter(
    var listBranch: ArrayList<Content>
): RecyclerView.Adapter<BranchesProjectAdapter.ViewHolder>() {

    private lateinit var branchesProjectCallBack: BranchesProjectCallBack

    inner class ViewHolder(val binding: BranchesProjectBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getBranch = listBranch[adapterPosition]
            branchesProjectCallBack.onClickBranch(getBranch.branchCode, getBranch.branchName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BranchesProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listBranch.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listBranch[position]

        holder.binding.tvCodeBranchesProject.text = response.branchCode
        holder.binding.tvNameBranchesProject.text = response.branchName
        holder.binding.tvTotBranchesProject.text = "Total project : ${response.totalProject}"
        val totPercentActive = response.percentageProjectAktif + response.percentageProjectNearExpired
        holder.binding.tvPercentActiveBranchesProject.text = "${"%.2f".format(totPercentActive)}%"
        holder.binding.tvTotActiveBranchesProject.text = "${response.totalProjectAktif + response.totalProjectNearExpired}"
        holder.binding.tvPercentClosedBranchesProject.text = "${"%.2f".format(response.percentageProjectClosed)}%"
        holder.binding.tvTotClosedBranchesProject.text = "${response.totalProjectClosed}"

    }

    fun setListener(branchesProjectCallBack: BranchesProjectCallBack) {
        this.branchesProjectCallBack = branchesProjectCallBack
    }

    interface BranchesProjectCallBack{
        fun onClickBranch(branchCode: String, branchName: String)
    }
}