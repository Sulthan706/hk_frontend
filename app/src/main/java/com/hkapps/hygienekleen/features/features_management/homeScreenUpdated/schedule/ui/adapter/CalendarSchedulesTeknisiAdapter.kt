package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.Content

class CalendarSchedulesTeknisiAdapter(
    val listSchedule: ArrayList<Content>
): RecyclerView.Adapter<CalendarSchedulesTeknisiAdapter.ViewHolder>() {

    inner class ViewHolder (
        val binding: ListScheduleManagementBinding
    ): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListScheduleManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listSchedule.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = listSchedule[position]
        holder.binding.tvProjectNameListScheduleManagement.text = schedule.projectName
        holder.binding.tvProjectCodeListScheduleManagement.text = schedule.projectCode
        holder.binding.tvTimeInScheduleManagement.text = schedule.scanIn ?: "--:--"
        holder.binding.tvTimeOutScheduleManagement.text = schedule.scanOut ?: "--:--"

        holder.binding.tvDivertedInfoScheduleManagement.visibility = View.INVISIBLE

        if (schedule.divertedTo == null || schedule.divertedTo == "null" || schedule.divertedTo == "") {
            holder.binding.tvDivertedInfoScheduleManagement.visibility = View.INVISIBLE
        } else holder.binding.tvDivertedInfoScheduleManagement.visibility = View.VISIBLE
    }

}