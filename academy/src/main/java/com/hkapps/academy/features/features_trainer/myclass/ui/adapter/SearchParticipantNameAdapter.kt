package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ListParticipantBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.Content

class SearchParticipantNameAdapter(
    var listParticipant: ArrayList<Content>
): RecyclerView.Adapter<SearchParticipantNameAdapter.ViewHolder>() {
    inner class ViewHolder (val binding: ListParticipantBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listParticipant.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listParticipant[position]

        holder.binding.tvNameListParticipant.text = response.name
        holder.binding.tvPositionProjectListParticipant.text = "${response.jabatan} | ${response.projectCode}"
    }
}