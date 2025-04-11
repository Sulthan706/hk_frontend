package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ListParticipantTrainingScoreBinding
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.Content
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst

class ParticipantTrainingScoreAdapter(
    var listParticipant: ArrayList<Content>
): RecyclerView.Adapter<ParticipantTrainingScoreAdapter.ViewHolder>() {

    private lateinit var participantScoreCallBack: ParticipantScoreCallBack
    private val statusTraining = AcademyOperationPref.loadString(AcademyOperationPrefConst.STATUS_DETAIL_TRAINING, "")

    inner class ViewHolder (val binding: ListParticipantTrainingScoreBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListParticipantTrainingScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listParticipant.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listParticipant[position]

        holder.binding.tvNameParticipantsTrainingScore.text = response.name
        holder.binding.tvPositionParticipantsTrainingScore.text = response.jabatan

        if (statusTraining == "Selesai") {
            if (response.nilai == null || response.nilai == 0) {
                holder.binding.tvScoreParticipantsTrainingScore.setBackgroundResource(R.drawable.bg_rounded_blue5)
                holder.binding.tvScoreParticipantsTrainingScore.setTextColor(Color.parseColor("#FFFFFF"))
                holder.binding.tvScoreParticipantsTrainingScore.text = "Beri Nilai"

                holder.binding.tvScoreParticipantsTrainingScore.setOnClickListener {
                    participantScoreCallBack.onClickParticipant(response.participantId, response.name, response.jabatan)
                }
            } else {
                holder.binding.tvScoreParticipantsTrainingScore.setBackgroundResource(R.drawable.bg_field_blue5)
                holder.binding.tvScoreParticipantsTrainingScore.setTextColor(Color.parseColor("#2F80ED"))
                holder.binding.tvScoreParticipantsTrainingScore.text = response.nilai.toString()
            }
        } else {
            holder.binding.tvScoreParticipantsTrainingScore.setBackgroundResource(R.drawable.bg_rounded_grey_disable)
            holder.binding.tvScoreParticipantsTrainingScore.setTextColor(Color.parseColor("#FFFFFF"))
            holder.binding.tvScoreParticipantsTrainingScore.text = "Beri Nilai"
        }

    }

    fun setListener(participantScoreCallBack: ParticipantScoreCallBack) {
        this.participantScoreCallBack = participantScoreCallBack
    }

    interface ParticipantScoreCallBack {
        fun onClickParticipant(participantId: Int, participantName: String, participantPosition: String)
    }

}