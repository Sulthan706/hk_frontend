package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListMeetingInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listMeeting.Content

class ListMeetingInspeksiAdapter(
    private val context: Context,
    var listMeeting: ArrayList<Content>
): RecyclerView.Adapter<ListMeetingInspeksiAdapter.ViewHolder>() {

    private lateinit var listMeetingCallBack: ListMeetingCallBack

    inner class ViewHolder(val binding: ListMeetingInspeksiBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listMeeting[adapterPosition]
            listMeetingCallBack.onClickItem(selected.idMeeting)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListMeetingInspeksiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listMeeting[position]

        holder.binding.tvDateItemListMeetingInspeksi.text = response.startMeetingDate
        holder.binding.tvTimeItemListMeetingInspeksi.text = "${response.startMeetingTime} - ${response.endMeetingTime}"
        holder.binding.tvTopicItemListMeetingInspeksi.text = response.titleMeeting
    }

    override fun getItemCount(): Int {
        return listMeeting.size
    }

    fun setListener(listMeetingCallBack: ListMeetingCallBack) {
        this.listMeetingCallBack = listMeetingCallBack
    }

    interface ListMeetingCallBack {
        fun onClickItem(idMeeting: Int)
    }
}