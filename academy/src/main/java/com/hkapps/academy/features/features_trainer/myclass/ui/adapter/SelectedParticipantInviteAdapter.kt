package com.hkapps.academy.features.features_trainer.myclass.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ItemSelectedInviteParticipantBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ContentParcelable

class SelectedParticipantInviteAdapter(
    var listSelected: ArrayList<ContentParcelable>
): RecyclerView.Adapter<SelectedParticipantInviteAdapter.ViewHolder>() {

    private lateinit var listSelectedCallBack: ListSelectedCallBack

    inner class ViewHolder (val binding: ItemSelectedInviteParticipantBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val getSelected = listSelected[adapterPosition]
            listSelectedCallBack.onClickSelected(getSelected.userNuc)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSelectedInviteParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listSelected.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listSelected[position]

        holder.binding.tvSelectedParticipant.text = response.name

    }

    fun setListener (listSelectedCallBack: ListSelectedCallBack) {
        this.listSelectedCallBack = listSelectedCallBack
    }

    interface ListSelectedCallBack {
        fun onClickSelected (participantNuc: String)
    }
}