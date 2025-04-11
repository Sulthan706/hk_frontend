package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListClientMeetingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.Data

class ClientsSendRoutineAdapter(
    private val context: Context,
    var listClient: ArrayList<Data>
): RecyclerView.Adapter<ClientsSendRoutineAdapter.ViewHolder>() {

    private lateinit var clientsSendMailCallback: ClientsSendMailCallback

    inner class ViewHolder(val binding: ListClientMeetingBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listClient[adapterPosition]
            clientsSendMailCallback.onClickClientsRoutine(selected.clientId, selected.clientName, selected.clientEmail)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListClientMeetingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listClient[position]

        holder.binding.tvNameListClientMeeting.text = response.clientName
        holder.binding.tvMailListClientMeeting.text = response.clientEmail
        holder.binding.ivListClientMeeting.setImageDrawable(context.resources.getDrawable(R.drawable.ic_delete))

    }

    override fun getItemCount(): Int {
        return listClient.size
    }

    fun setListener (clientsSendMailCallback: ClientsSendMailCallback) {
        this.clientsSendMailCallback = clientsSendMailCallback
    }

    interface ClientsSendMailCallback {
        fun onClickClientsRoutine (idClient: Int, clientName: String, clientEmail: String, )
    }

}