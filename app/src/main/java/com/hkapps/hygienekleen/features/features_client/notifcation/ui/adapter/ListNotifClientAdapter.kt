package com.hkapps.hygienekleen.features.features_client.notifcation.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemNotificationClientsBinding
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ContentListNotifClient

class ListNotifClientAdapter(
    private val context: Context,
    var listNotifClients: ArrayList<ContentListNotifClient>
):
RecyclerView.Adapter<ListNotifClientAdapter.ViewHolder>(){
    var selectedItem = -1
    private lateinit var listNotifClient: ListNotifClient

    inner class ViewHolder(val binding: ItemNotificationClientsBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val putNotifClientRead = listNotifClients[adapterPosition]
            listNotifClient.onClickNotifClient(
                putNotifClientRead.notificationHistoryId,
                putNotifClientRead.isRead,
                putNotifClientRead.relationId

            )
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType: Int
    ): ListNotifClientAdapter.ViewHolder {
        return ViewHolder(
            ItemNotificationClientsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val item = listNotifClients[position]
            binding.tvCaptionContentNotif.text = "Cek info selengkapnya pada menu CTalk."
            if (item.isRead == "N"){
                if(selectedItem == position){
                    Log.d("selec", "$selectedItem")
                    binding.clNotificationMidHistory.setBackgroundColor(Color.parseColor("#DAE8FD"))
                } else {
                    binding.clNotificationMidHistory.setBackgroundColor(Color.parseColor("#DAE8FD"))
                }
            } else {
                binding.clNotificationMidHistory.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }




            if(item.notificationType == "COMPLAINT CLIENT"){
                binding.tvStatusNotif.text = "CTalk"
            }
            binding.tvMainContentNotif.text = item.notificationContent
            binding.tvDateStatusNotif.text = item.createdAt

        }
    }

    override fun getItemCount(): Int {
        return listNotifClients.size
    }

    fun setListener(listNotifClients: ListNotifClient){
        this.listNotifClient = listNotifClients
    }

    interface ListNotifClient {
        fun onClickNotifClient(notificationHistoryId: Int, isRead: String, relationId: Int,)
    }
}


