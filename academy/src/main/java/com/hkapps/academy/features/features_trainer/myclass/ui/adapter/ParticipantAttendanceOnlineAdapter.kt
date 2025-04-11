package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ListParticipantAttendanceOnlineBinding
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.Content

class ParticipantAttendanceOnlineAdapter(
    var listParticipant: ArrayList<Content>
): RecyclerView.Adapter<ParticipantAttendanceOnlineAdapter.ViewHolder>() {

    private lateinit var participantAttendanceCallBack: ParticipantsAttendanceCallBack

    inner class ViewHolder (val binding: ListParticipantAttendanceOnlineBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListParticipantAttendanceOnlineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listParticipant.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listParticipant[position]

        holder.binding.tvNameParticipantsAttendanceOnline.text = response.name
        holder.binding.tvPositionParticipantsAttendanceOnline.text = response.jabatan

        when(response.statusAttendance) {
            "BELUM_ABSEN" -> {
                holder.binding.ivCheckParticipantsAttendanceOnline.setImageResource(R.drawable.ic_round_check_grey)
                holder.binding.ivCloseParticipantsAttendanceOnline.setImageResource(R.drawable.ic_round_close_grey)
            }
            "HADIR" -> {
                holder.binding.ivCheckParticipantsAttendanceOnline.setImageResource(R.drawable.ic_round_check_green)
                holder.binding.ivCloseParticipantsAttendanceOnline.setImageResource(R.drawable.ic_round_close_grey)
            }
            "TIDAK_HADIR" -> {
                holder.binding.ivCheckParticipantsAttendanceOnline.setImageResource(R.drawable.ic_round_check_grey)
                holder.binding.ivCloseParticipantsAttendanceOnline.setImageResource(R.drawable.ic_round_close_red)
            }
        }

        holder.binding.ivCheckParticipantsAttendanceOnline.setOnClickListener {
            participantAttendanceCallBack.onClickParticipant("HADIR", response.participantId)
        }
        holder.binding.ivCloseParticipantsAttendanceOnline.setOnClickListener {
            participantAttendanceCallBack.onClickParticipant("TIDAK_HADIR", response.participantId)
        }

    }

    fun setListener(participantAttendanceCallBack: ParticipantsAttendanceCallBack) {
        this.participantAttendanceCallBack = participantAttendanceCallBack
    }

    interface ParticipantsAttendanceCallBack {
        fun onClickParticipant (attendance: String, participantId: Int)
    }
}