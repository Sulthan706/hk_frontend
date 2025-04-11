package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceHistoryByDateBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.HistoryDataArrayByDateResponseModel
import kotlin.collections.ArrayList


class AttendanceHistoryByDateAdapter(
    private var historyDataArrayByDateResponseModel: ArrayList<HistoryDataArrayByDateResponseModel>,
) :
    RecyclerView.Adapter<AttendanceHistoryByDateAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceHistoryByDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(historyDataArrayByDateResponseModel[position]) {
                val response = historyDataArrayByDateResponseModel[position]
                holder.binding.tvHistoryProject.text = "Project Testing coba dulu gansssssss"
                holder.binding.tvTimeItemDailyAttendance.text = response.createdAt
                holder.binding.tvHistoryCheckin.text = response.scanIn
                holder.binding.tvHistoryCheckout.text = response.scanOut

                if (response.scanOut == null || response.scanOut == "" || response.scanOut == "null") {
                    holder.binding.tvHistoryCheckout.text = "Belum Absen"
                } else {
                    holder.binding.tvHistoryCheckout.text = response.scanOut
                }

                holder.binding.layoutItemAttendanceDaily.setOnClickListener {
//                    android.widget.Toast.makeText(
//                        context,
//                        "" + historyDataArrayByDateResponseModel[position].attendanceId,
//                        android.widget.Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return historyDataArrayByDateResponseModel.size
    }

    inner class ViewHolder(val binding: ItemAttendanceHistoryByDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}