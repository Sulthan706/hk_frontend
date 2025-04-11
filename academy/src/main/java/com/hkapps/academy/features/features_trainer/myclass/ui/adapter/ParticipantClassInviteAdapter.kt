package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ListParticipantBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.Content

class ParticipantClassInviteAdapter(
    var listParticipant: ArrayList<Content>
): RecyclerView.Adapter<ParticipantClassInviteAdapter.ViewHolder>() {

    private lateinit var listParticipantCallBack: ListParticipantCallBack

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

        holder.binding.clListParticipant.setOnClickListener {
            if (holder.binding.ivCheckListParticipant.isGone) {
                holder.binding.ivCheckListParticipant.visibility = View.VISIBLE
            } else {
                holder.binding.ivCheckListParticipant.visibility = View.GONE
            }

            listParticipantCallBack.onClickParticipant(
                response.userNuc,
                response.name,
                response.email ?: "",
                response.role,
                response.levelJabatan,
                response.jabatan,
                response.projectCode ?: ""
            )
        }

    }

    fun setListener (listParticipantCallBack: ListParticipantCallBack) {
        this.listParticipantCallBack = listParticipantCallBack
    }

    interface ListParticipantCallBack {
        fun onClickParticipant (
            participantNuc: String,
            participantName: String,
            email: String,
            role: String,
            position: String,
            jabatan: String,
            projectCode: String
        )
    }
}