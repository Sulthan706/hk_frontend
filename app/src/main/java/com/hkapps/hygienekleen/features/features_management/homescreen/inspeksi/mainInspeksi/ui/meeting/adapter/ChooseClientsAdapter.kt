package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListClientMeetingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.fragment.BotSheetChooseClientFragment

class ChooseClientsAdapter(
    private val context: Context,
    var listClient: ArrayList<Data>
): RecyclerView.Adapter<ChooseClientsAdapter.ViewHolder>() {

    private lateinit var chooseClientsCallback: BotSheetChooseClientFragment

    inner class ViewHolder(val binding: ListClientMeetingBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listClient[adapterPosition]
            chooseClientsCallback.onChooseClient(selected.clientId, selected.clientName, selected.clientEmail, binding.ivListClientMeeting)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListClientMeetingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listClient[position]

        holder.binding.tvNameListClientMeeting.text = response.clientName
        holder.binding.tvMailListClientMeeting.text = response.clientEmail

    }

    override fun getItemCount(): Int {
        return listClient.size
    }

    fun setListener (chooseClientsCallback: BotSheetChooseClientFragment) {
        this.chooseClientsCallback = chooseClientsCallback
    }

    interface ChooseClientsCallback {
        fun onChooseClient(clientId: Int, clientName: String, clientEmail: String, imageView: ImageView)
    }

}