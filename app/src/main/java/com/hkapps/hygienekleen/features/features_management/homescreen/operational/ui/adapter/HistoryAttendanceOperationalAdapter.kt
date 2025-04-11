package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceHistoryNewBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance.Data

class HistoryAttendanceOperationalAdapter(
    private var historyAttendance: ArrayList<Data>,
): RecyclerView.Adapter<HistoryAttendanceOperationalAdapter.ViewHolder>() {
    private var context: Context? = null

    inner class ViewHolder(val binding: ItemAttendanceHistoryNewBinding):
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAttendanceOperationalAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceHistoryNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: HistoryAttendanceOperationalAdapter.ViewHolder,
        position: Int
    ) {
        with(holder) {
            with(historyAttendance[position]) {
                val response = historyAttendance[position]
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
                                com.hkapps.hygienekleen.R.color.enabledcalendar
                            )
                        )
                    };
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return historyAttendance.size
    }
}