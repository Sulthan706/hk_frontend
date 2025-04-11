package com.hkapps.academy.features.features_participants.homescreen.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ItemTrainingHomeBinding
import com.hkapps.academy.features.features_participants.homescreen.home.model.listTraining.Content
import java.text.SimpleDateFormat

class TrainingsHomeAdapter(
    var listTraining: ArrayList<Content>
): RecyclerView.Adapter<TrainingsHomeAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: ItemTrainingHomeBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTrainingHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listTraining.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listTraining[position]
        val sdfBefore = SimpleDateFormat("yyyy-MM-dd")
        val dateParam = sdfBefore.parse(response.trainingDate)
        val sdfAfter = SimpleDateFormat("dd MMM yyyy")


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
        holder.binding.tvTimeItemTraining.text = "${response.trainingStart} - ${response.trainingEnd}"
        holder.binding.tvTimeTraining.text = "${response.durationInMinute} menit"
        holder.binding.tvTitleTraining.text = response.trainingName
        holder.binding.tvMateriTraining.text = response.moduleName
        holder.binding.tvScheduleTraining.text = sdfAfter.format(dateParam)
        holder.binding.tvPeopleTraining.text = response.trainerName

    }


}