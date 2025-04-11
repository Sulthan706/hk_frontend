package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListTrackingProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.Data

class AllAttendanceTrackingAdapter(var listAttendance: ArrayList<Data>, private val visibilityDate: String):
    RecyclerView.Adapter<AllAttendanceTrackingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListTrackingProjectBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListTrackingProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAttendance[position]

        if (visibilityDate == "VISIBLE") {
            holder.binding.llDateListTrackingProject.visibility = View.VISIBLE
        } else {
            holder.binding.llDateListTrackingProject.visibility = View.GONE
        }

        holder.binding.tvProjectListTrackingProject.text = response.projectName
        holder.binding.tvDateListTrackingProject.text = response.date
        holder.binding.tvTimeInListTrackingProject.text = if (response.checkIn == "" || response.checkIn == "null" || response.checkIn == null) {
            "--:--"
        } else {
            response.checkIn
        }
        holder.binding.tvTimeOutListTrackingProject.text = if (response.checkOut == "" || response.checkOut == "null" || response.checkOut == null) {
            "--:--"
        } else {
            response.checkOut
        }
    }

    override fun getItemCount(): Int {
        return listAttendance.size
    }
}