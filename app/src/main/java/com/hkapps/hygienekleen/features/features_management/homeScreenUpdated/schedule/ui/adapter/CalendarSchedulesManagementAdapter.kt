package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listSchedule.Content

class CalendarSchedulesManagementAdapter (
    val listSchedule: ArrayList<Content>
): RecyclerView.Adapter<CalendarSchedulesManagementAdapter.ViewHolder>() {

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
        holder.binding.tvTimeInScheduleManagement.text = schedule.attendanceIn ?: "--:--"
        holder.binding.tvTimeOutScheduleManagement.text = schedule.attendanceOut ?: "--:--"

        if (schedule.diverted) {
            holder.binding.tvDivertedInfoScheduleManagement.visibility = View.VISIBLE
        } else {
            holder.binding.tvDivertedInfoScheduleManagement.visibility = View.GONE
        }
    }

}