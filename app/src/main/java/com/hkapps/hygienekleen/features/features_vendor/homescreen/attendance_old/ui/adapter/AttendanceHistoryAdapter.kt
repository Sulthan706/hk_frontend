package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceHistoryBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.HistoryDataArrayResponseModel
import java.text.SimpleDateFormat
import java.util.*


class AttendanceHistoryAdapter(
    private var historyDataArrayResponseModel: ArrayList<HistoryDataArrayResponseModel>,
) :
    RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(historyDataArrayResponseModel[position]) {
                val response = historyDataArrayResponseModel[position]
                holder.binding.tvTimeItemDailyAttendance.text = response.createdAt
                holder.binding.tvHistoryCheckin.text = response.scanIn
                holder.binding.tvHistoryCheckout.text = response.scanOut

                val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                Log.d("Attendancehistory", "date: $currentDate")

                if (response.createdAt == currentDate){
                    Log.d("Attendancehistory", "" + response.createdAt)
                    holder.binding.clItemAttendanceHistory.visibility = View.VISIBLE

                    if (response.scanOut == null || response.scanOut == "" || response.scanOut == "null"){
                        holder.binding.tvHistoryCheckout.text = "Belum Absen"
                    }else{
                        holder.binding.tvHistoryCheckout.text = response.scanOut
                    }
                }else{
                    holder.binding.clItemAttendanceHistory.visibility = View.GONE
                    holder.binding.tvItemAttendanceHistory.visibility = View.VISIBLE
                }

                holder.binding.layoutItemAttendanceDaily.setOnClickListener {
//                    android.widget.Toast.makeText(
//                        context,
//                        "" + historyDataArrayResponseModel[position].attendanceId,
//                        android.widget.Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return historyDataArrayResponseModel.size
    }

    inner class ViewHolder(val binding: ItemAttendanceHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}