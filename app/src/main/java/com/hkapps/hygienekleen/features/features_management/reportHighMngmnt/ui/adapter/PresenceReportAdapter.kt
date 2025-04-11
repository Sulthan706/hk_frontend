package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemTableAttendanceReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.Content

class PresenceReportAdapter(
    var listPresence: ArrayList<Content>,
    private val offset: Int
): RecyclerView.Adapter<PresenceReportAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTableAttendanceReportManagementBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTableAttendanceReportManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listPresence.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listPresence[position]

        holder.binding.tvNoAttendanceReport.text = response.rowNumber.toString()
        holder.binding.tvNucAttendanceReport.text = response.employeeCode
        holder.binding.tvNameAttendanceReport.text = response.employeeName
        holder.binding.tvProjectAttendanceReport.text = response.projectName
        holder.binding.tvHkAttendanceReport.text = response.totalHari.toString()
        holder.binding.tvPresenceAttendanceReport.text = "${response.hadirCount} hari"
        holder.binding.tvPresenceTotAttendanceReport.text = if (String.format("%.2f", response.persentaseKehadiran) == "100.00") {
            "100%"
        } else {
            "${String.format("%.2f", response.persentaseKehadiran)}%"
        }

        if (response.terlambatCount == 0) {
            holder.binding.tvNoLateAttendanceReport.visibility = View.VISIBLE
            holder.binding.tvLateAttendanceReport.visibility = View.GONE
            holder.binding.tvLateTotAttendanceReport.visibility = View.GONE
        } else {
            holder.binding.tvNoLateAttendanceReport.visibility = View.GONE
            holder.binding.tvLateAttendanceReport.visibility = View.VISIBLE
            holder.binding.tvLateTotAttendanceReport.visibility = View.VISIBLE

            holder.binding.tvLateAttendanceReport.text = "${response.terlambatCount} kali"
            holder.binding.tvLateTotAttendanceReport.text = if (String.format("%.2f", response.persentaseTerlambat) == "100.00") {
                "100%"
            } else {
                "${String.format("%.2f", response.persentaseTerlambat)}%"
            }
        }

    }
}