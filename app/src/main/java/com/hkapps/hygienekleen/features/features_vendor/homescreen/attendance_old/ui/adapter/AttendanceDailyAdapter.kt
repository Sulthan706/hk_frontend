package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceDailyBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActDataArrayResponseModel
import kotlin.collections.ArrayList


class AttendanceDailyAdapter(
    private var dailyActDataArrayResponseModel: ArrayList<DailyActDataArrayResponseModel>,
) :
    RecyclerView.Adapter<AttendanceDailyAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceDailyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dailyActDataArrayResponseModel[position]) {
                val response = dailyActDataArrayResponseModel[position]
                holder.binding.tvItemDailyAttendance.text = response.activity
//                holder.binding.tvTimeItemDailyAttendance.text = response.startAt + " - " + response.endAt

                holder.binding.layoutItemAttendanceDaily.setOnClickListener {
                    android.widget.Toast.makeText(context,""+dailyActDataArrayResponseModel[position].activity, android.widget.Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyActDataArrayResponseModel.take(3).size
    }

    inner class ViewHolder(val binding: ItemAttendanceDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}