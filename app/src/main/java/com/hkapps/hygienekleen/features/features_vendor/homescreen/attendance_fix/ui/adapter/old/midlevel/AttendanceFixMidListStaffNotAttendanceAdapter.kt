package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.old.midlevel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceStaffNotAttendanceBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.EmployeeNotAttendance
import kotlin.collections.ArrayList


class AttendanceFixMidListStaffNotAttendanceAdapter(
    private var staffNotAttendance: ArrayList<EmployeeNotAttendance>,
) :
    RecyclerView.Adapter<AttendanceFixMidListStaffNotAttendanceAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceStaffNotAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(staffNotAttendance[position]) {
                val response = staffNotAttendance[position]
                binding.tvStaffNotAttendanceName.text = response.employeeName
                binding.tvStaffNotAttendanceJobCode.text = response.employeeCode
            }
        }
    }

    override fun getItemCount(): Int {
        return staffNotAttendance.size
    }

    inner class ViewHolder(val binding: ItemAttendanceStaffNotAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root)

}