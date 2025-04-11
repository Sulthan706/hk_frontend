package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListVisitProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.Content

class ListUnvisitedManagementAdapter(
    var listSchedule: ArrayList<Content>
) : RecyclerView.Adapter<ListUnvisitedManagementAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListVisitProjectManagementBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListVisitProjectManagementBinding.inflate(LayoutInflater.from(
            parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listSchedule.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listSchedule[position]

        holder.binding.tvBranchListVisitProjectManagement.text = response.branchName
        holder.binding.tvProjectListVisitProjectManagement.text = response.projectName
        holder.binding.tvCheckInListVisitProjectManagement.text = response.scanIn ?: "--:--"
        holder.binding.tvCheckOutListVisitProjectManagement.text = response.scanOut ?: "--:--"
    }
}