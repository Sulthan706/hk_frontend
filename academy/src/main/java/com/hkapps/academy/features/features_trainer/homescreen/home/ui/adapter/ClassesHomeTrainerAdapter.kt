package com.hkapps.academy.features.features_trainer.homescreen.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ItemTrainingHomeBinding
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.Content
import java.text.SimpleDateFormat

class ClassesHomeTrainerAdapter(
    var listClass: ArrayList<Content>
): RecyclerView.Adapter<ClassesHomeTrainerAdapter.ViewHolder>() {
    inner class ViewHolder (val binding: ItemTrainingHomeBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTrainingHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listClass.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listClass[position]
        val sdfBefore = SimpleDateFormat("yyyy-MM-dd")
        val dateParam = sdfBefore.parse(response.trainingDate)
        val sdfAfter = SimpleDateFormat("dd MMM yyyy")

        // set visible participant textview
        holder.binding.tvParticipantTraining.visibility = View.VISIBLE
        holder.binding.llPeopleTraining.visibility = View.GONE

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
        holder.binding.tvTimeItemTraining.text = "${response.trainingStart} - ${response.trainingEnd} ${response.region}"
        holder.binding.tvTimeTraining.text = "${response.durationInMinute} menit"
        holder.binding.tvTitleTraining.text = response.trainingName
        holder.binding.tvMateriTraining.text = response.moduleName
        holder.binding.tvScheduleTraining.text = sdfAfter.format(dateParam)
        holder.binding.tvParticipantTraining.text = "${response.quota} peserta"
    }
}