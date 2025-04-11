package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListClientMeetingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosing

class ChooseClientClosingAdapter(
    private var listClient : ArrayList<ClientClosing>,
    private val listener : OnClickChooseClientClosing
) : RecyclerView.Adapter<ChooseClientClosingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListClientMeetingBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listClient[adapterPosition]
            listener.onChecked(selected, binding.ivListClientMeeting)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListClientMeetingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listClient[position]

        holder.binding.tvNameListClientMeeting.text = response.clientName
        holder.binding.tvMailListClientMeeting.text = response.email

    }

    override fun getItemCount(): Int {
        return listClient.size
    }


    interface OnClickChooseClientClosing{
        fun onChecked(clientClosing: ClientClosing, imageView: ImageView)
    }
}