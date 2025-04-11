package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ListParticipantAttendanceOnsiteBinding
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.Content

class ParticipantAttendanceOnsiteAdapter(
    var listParticipant: ArrayList<Content>
): RecyclerView.Adapter<ParticipantAttendanceOnsiteAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: ListParticipantAttendanceOnsiteBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListParticipantAttendanceOnsiteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listParticipant.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listParticipant[position]

        holder.binding.tvNameParticipantsAttendanceOnsite.text = response.name
        holder.binding.tvPositionParticipantsAttendanceOnsite.text = response.jabatan

        when(response.statusAttendance) {
            "BELUM_ABSEN" -> {
                holder.binding.tvStatusParticipantsAttendanceOnsite.text = "-"
                holder.binding.tvTimeParticipantsAttendanceOnsite.text = "-"
            }
            "HADIR" -> {
                holder.binding.tvStatusParticipantsAttendanceOnsite.text = "HADIR"
                holder.binding.tvTimeParticipantsAttendanceOnsite.text = "-"
            }
            "TIDAK_HADIR" -> {
                holder.binding.tvStatusParticipantsAttendanceOnsite.text = "TIDAK HADIR"
                holder.binding.tvTimeParticipantsAttendanceOnsite.text = "-"
            }
        }

    }

}