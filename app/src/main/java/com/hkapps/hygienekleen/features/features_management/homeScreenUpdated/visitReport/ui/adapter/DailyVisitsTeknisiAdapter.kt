package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.Content

class DailyVisitsTeknisiAdapter(
    var dailyVisits: ArrayList<Content>
) : RecyclerView.Adapter<DailyVisitsTeknisiAdapter.ViewHolder>(){

    inner class ViewHolder (val binding: ListScheduleManagementBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListScheduleManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return dailyVisits.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = dailyVisits[position]

        holder.binding.tvPlanningInfoScheduleManagement.visibility = View.VISIBLE
        holder.binding.tvDivertedInfoScheduleManagement.visibility = View.VISIBLE

        holder.binding.tvProjectNameListScheduleManagement.text = response.projectName
        holder.binding.tvProjectCodeListScheduleManagement.text = response.projectCode
        holder.binding.tvTimeInScheduleManagement.text = response.scanIn ?: "--:--"
        holder.binding.tvTimeOutScheduleManagement.text = response.scanOut ?: "--:--"

        holder.binding.tvPlanningInfoScheduleManagement.text = if (response.divertedTo == null || response.divertedTo == "null" || response.divertedTo == "") {
            "Planned"
        } else "Unplanned"

        if (response.status == "HADIR") {
            holder.binding.tvDivertedInfoScheduleManagement.text = "Realization"
            holder.binding.tvDivertedInfoScheduleManagement.setTextColor(android.graphics.Color.parseColor("#00BD8C"))
        } else {
            holder.binding.tvDivertedInfoScheduleManagement.text = "Unvisited"
            holder.binding.tvDivertedInfoScheduleManagement.setTextColor(android.graphics.Color.parseColor("#FF2727"))
        }
    }
}