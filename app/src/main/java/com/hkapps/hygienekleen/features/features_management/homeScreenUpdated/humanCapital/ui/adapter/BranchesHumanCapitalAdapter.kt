package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.BranchesHumanCapitalBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.Content

class BranchesHumanCapitalAdapter(
    var listBranch: ArrayList<Content>
): RecyclerView.Adapter<BranchesHumanCapitalAdapter.ViewHolder>() {

    private lateinit var branchesHumanCapitalCallBack: BranchesHumanCapitalCallBack

    inner class ViewHolder(val binding: BranchesHumanCapitalBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getBranch = listBranch[adapterPosition]
            branchesHumanCapitalCallBack.onClickBranch(getBranch.branchCode, getBranch.branchName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BranchesHumanCapitalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listBranch.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listBranch[position]

        holder.binding.tvCodeBranchesHumanCapital.text = response.branchCode
        holder.binding.tvNameBranchesHumanCapital.text = response.branchName
        val formattedNumber = "%,d".format(response.totalEmployeeAktif)
        holder.binding.tvTotMpBranchesHumanCapital.text = "Total MP : $formattedNumber"
        holder.binding.tvTotResignBranchesHumanCapital.text = "${response.totalEmployeeResign}"
        holder.binding.tvTotNewcomerBranchesHumanCapital.text = "${response.totalEmployeeNew}"
        if (response.totalEmployeeResign > response.totalEmployeeNew) {
            holder.binding.tvTotTurnoverBranchesHumanCapital.setTextColor(Color.parseColor("#FF2727"))
            holder.binding.tvTotTurnoverBranchesHumanCapital.text = "-${response.totalTurnover}"
        } else if (response.totalEmployeeResign < response.totalEmployeeNew) {
            holder.binding.tvTotTurnoverBranchesHumanCapital.setTextColor(Color.parseColor("#007AFF"))
            holder.binding.tvTotTurnoverBranchesHumanCapital.text = "+${response.totalTurnover}"
        } else {
            holder.binding.tvTotTurnoverBranchesHumanCapital.setTextColor(Color.parseColor("#00BD8C"))
            holder.binding.tvTotTurnoverBranchesHumanCapital.text = "="
        }
    }

    fun setListener(branchesHumanCapitalCallBack: BranchesHumanCapitalCallBack) {
        this.branchesHumanCapitalCallBack = branchesHumanCapitalCallBack
    }

    interface BranchesHumanCapitalCallBack{
        fun onClickBranch(branchCode: String, branchName: String)
    }
}