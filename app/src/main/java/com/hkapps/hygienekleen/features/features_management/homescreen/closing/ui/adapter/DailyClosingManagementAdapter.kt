package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemDailyClosingSpvBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyTargetManagement
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ListDailyTargetClosing

class DailyClosingManagementAdapter(
    private val ctx : Context,
    val data : MutableList<ListDailyTargetClosing>,
    private val listener : OnClickDailyClosingManagement,
) : RecyclerView.Adapter<DailyClosingManagementAdapter.DailyClosingManagementViewHolder>() {

    inner class DailyClosingManagementViewHolder(val binding : ListItemDailyClosingSpvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyClosingManagementViewHolder {
        return DailyClosingManagementViewHolder(ListItemDailyClosingSpvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DailyClosingManagementViewHolder, position: Int) {
        holder.binding.apply {
            projectName.text = data[position].projectName
            projectId.text = data[position].projectCode
            tvLocation.text = data[position].branchName
            val closingAdapter = DailyClosingInsideManagementAdapter(data[position].listTarget,listener)
            rvDailyTarget.apply {
                adapter = closingAdapter
                layoutManager = LinearLayoutManager(ctx)
            }

        }
    }

    interface OnClickDailyClosingManagement{
        fun onCLickDetail(data : DailyTargetManagement,isYesterday : Boolean)

        fun onClickDetailYesterday(data : DailyTargetManagement)

        fun onClickHistory(data : DailyTargetManagement)
    }
}