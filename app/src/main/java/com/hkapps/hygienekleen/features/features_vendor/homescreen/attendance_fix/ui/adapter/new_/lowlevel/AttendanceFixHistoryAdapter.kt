package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.new_.lowlevel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemAttendanceHistoryNewBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.HistoryDataArrayResponseModel
import java.text.SimpleDateFormat
import java.util.*


class AttendanceFixHistoryAdapter(
    private var historyDataArrayResponseModel: ArrayList<HistoryDataArrayResponseModel>,
) :
    RecyclerView.Adapter<AttendanceFixHistoryAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceHistoryNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(historyDataArrayResponseModel[position]) {
                val response = historyDataArrayResponseModel[position]
                holder.binding.tvHistoryProject.text = response.projectName
                holder.binding.tvTimeItemDailyAttendance.text = response.createdAt
                holder.binding.tvHistoryCheckin.text = response.scanIn
                holder.binding.tvHistoryCheckout.text = response.scanOut

                if (response.scanOut == null || response.scanOut == "" || response.scanOut == "null") {
                    holder.binding.tvHistoryCheckout.text = "Belum Absen"
                } else {
                    holder.binding.tvHistoryCheckout.text = response.scanOut
                }

                val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                println(" C DATE is  $currentDate")

                if (currentDate == response.createdAt) {
                    context?.resources?.let {
                        binding.layoutItemAttendanceDaily.setBackgroundColor(
                            it.getColor(
                                R.color.enabledcalendar
                            )
                        )
                    };
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
        return historyDataArrayResponseModel.size
    }

    inner class ViewHolder(val binding: ItemAttendanceHistoryNewBinding) :
        RecyclerView.ViewHolder(binding.root)

}