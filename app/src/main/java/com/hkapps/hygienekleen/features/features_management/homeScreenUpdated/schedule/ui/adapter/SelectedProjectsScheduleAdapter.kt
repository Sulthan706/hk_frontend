package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListCreateScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.SelectedProjectsSchedule

class SelectedProjectsScheduleAdapter(
    private val selectedProjects: ArrayList<SelectedProjectsSchedule>
) : RecyclerView.Adapter<SelectedProjectsScheduleAdapter.ViewHolder>(){

    private lateinit var selectedProjectsCallBack: SelectedProjectsCallBack
    var clickFrom = ""
    var total = 0

    inner class ViewHolder (val binding: ItemListCreateScheduleManagementBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListCreateScheduleManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return selectedProjects.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = selectedProjects[position]

        // validate count visit by date
        if (position == 0) {
            total = selectedProjects.count { it.date == response.date}
            holder.binding.clDateListCreateScheduleManagement.visibility = View.VISIBLE
            holder.binding.tvDateListCreateScheduleManagement.text = response.dateTxt
            holder.binding.tvVisitsListCreateScheduleManagement.text = "$total visits"
        } else {
            if (response.date == selectedProjects[position-1].date) {
                holder.binding.clDateListCreateScheduleManagement.visibility = View.GONE
            } else {
                total = selectedProjects.count { it.date == response.date}
                holder.binding.clDateListCreateScheduleManagement.visibility = View.VISIBLE
                holder.binding.tvDateListCreateScheduleManagement.text = response.dateTxt
                holder.binding.tvVisitsListCreateScheduleManagement.text = "$total visits"
            }
        }

        // set data visit
        holder.binding.tvProjectListCreateScheduleManagement.text = response.projectName
        holder.binding.tvProjectCodeListCreateScheduleManagement.text = response.projectCode

        holder.binding.tvEditListCreateScheduleManagement.setOnClickListener {
            clickFrom = "edit"
            val totalSelected = total
            selectedProjectsCallBack.onClickSelected(response.projectCode, response.date, response.dateTxt, clickFrom, totalSelected)
        }
        holder.binding.ivDeleteListCreateScheduleManagement.setOnClickListener {
            clickFrom = "delete"
            val totalSelected = total
            selectedProjectsCallBack.onClickSelected(response.projectCode, response.date, response.dateTxt, clickFrom, totalSelected)
        }

    }

    fun setListener(selectedProjectsCallBack: SelectedProjectsCallBack) {
        this.selectedProjectsCallBack = selectedProjectsCallBack
    }

    interface SelectedProjectsCallBack {
        fun onClickSelected(projectCode: String, date: String, dateTxt: String, clickFrom: String, totalProject: Int)
    }
}