package com.hkapps.academy.features.features_participants.training.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ItemTrainingScheduleBinding
import com.hkapps.academy.features.features_participants.training.model.listTraining.Content
import java.text.SimpleDateFormat

class TrainingsCalendarAdapter (
    var listTraining: ArrayList<Content>
): RecyclerView.Adapter<TrainingsCalendarAdapter.ViewHolder>() {
    inner class ViewHolder (val binding: ItemTrainingScheduleBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTrainingScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listTraining.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listTraining[position]
        val sdfBefore = SimpleDateFormat("yyyy-MM-dd")
        val dateParam = sdfBefore.parse(response.trainingDate)
        val sdfAfter = SimpleDateFormat("dd MMM yyyy")

        holder.binding.ivNextTrainingSchedule.visibility = View.VISIBLE
        holder.binding.tvDaysTraining.visibility = View.GONE
        holder.binding.tvApplyTraining.visibility = View.GONE

        when(response.jenisKelas) {
            "ONLINE" -> {
                holder.binding.tvStatusTraining.text = "Online"
                holder.binding.tvStatusTraining.setBackgroundResource(R.drawable.bg_rounded_blue5)
            }
            "ONSITE" -> {
                holder.binding.tvStatusTraining.text = "Onsite"
                holder.binding.tvStatusTraining.setBackgroundResource(R.drawable.bg_rounded_orange)
            }
        }
        holder.binding.tvTimeTraining.text = "${response.durationInMinute} menit"
        holder.binding.tvTitleTraining.text = response.trainingName
        holder.binding.tvMateriTraining.text = response.moduleName
        holder.binding.tvScheduleTraining.text = "${sdfAfter.format(dateParam)} | ${response.trainingStart} - ${response.trainingEnd}"
        holder.binding.tvPeopleTraining.text = response.trainerName
    }
}