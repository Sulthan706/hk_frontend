package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.new_.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemNotificationHistoryBinding
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.ContentNotificationMid

class NotifMidAdapter(
    private val context: Context,
    var notifMid: ArrayList<ContentNotificationMid>
) : RecyclerView.Adapter<NotifMidAdapter.ViewHolder>() {
    var selectedItem = -1
    private lateinit var listNotifDataHistory: ListNotifDataHistory

    inner class ViewHolder(val binding: ItemNotificationHistoryBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val putNotificationHistory = notifMid[adapterPosition]
            listNotifDataHistory.onClickNotification(
                putNotificationHistory.notificationHistoryId,
                putNotificationHistory.isRead,
                putNotificationHistory.notificationType,
                putNotificationHistory.relationId
            )

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            (ItemNotificationHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notifMid[position]

        if (item.isRead == "N") {
            if (selectedItem == position) {
                Log.d("selec", "$selectedItem")
                holder.binding.clNotificationMidHistory.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                holder.binding.clNotificationMidHistory.setBackgroundColor(Color.parseColor("#FFF7F2"))
            }
        } else {
            holder.binding.clNotificationMidHistory.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        //onclick detail notificaton
        when (item.notificationType) {
            "PERMISSION" -> {
                holder.binding.tvStatusNotif.text = "Izin"
                holder.binding.tvMainContentNotif.text = item.notificationContent
                when (item.notificationContent) {
                    "Permohonanmu telah diproses" -> {
                        holder.binding.tvCaptionContentNotif.text =
                            "Lihat pada izin saya untuk mengetahui status izinmu."
                    }
                    "Cek segera! Ada izin yang menunggu diproses" -> {
                        holder.binding.tvCaptionContentNotif.text =
                            "Lihat daftar selengkapnya pada menu izin."
                    }
                }
            }

            "OVERTIME" -> {
                holder.binding.tvStatusNotif.text = "Lembur"
                holder.binding.tvMainContentNotif.text = "Ada jadwal lembur untukmu"
                holder.binding.tvCaptionContentNotif.text =
                    "Cek info selengkapnya pada Home atau menu jadwal."
            }

            "COMPLAINT CLIENT" -> {
                holder.binding.tvStatusNotif.text = "CTalk"
                holder.binding.tvMainContentNotif.text = item.notificationContent
                holder.binding.tvCaptionContentNotif.text = "Cek info selengkapnya pada menu CTalk."
                if (item.projectName.isNotEmpty()) {
                    holder.binding.tvSecondMainContentNotif.visibility = View.VISIBLE
                    holder.binding.tvSecondMainContentNotif.text = "project: ${item.projectName}"
                } else {
                    holder.binding.tvSecondMainContentNotif.visibility = View.GONE
                }
            }

            "COMPLAINT INTERNAL" -> {
                holder.binding.tvStatusNotif.text = "CFTalk"
                holder.binding.tvMainContentNotif.text = item.notificationContent
                holder.binding.tvCaptionContentNotif.text =
                    "Cek info selengkapnya pada menu CFTalk."
                if (item.projectName.isNotEmpty()) {
                    holder.binding.tvSecondMainContentNotif.visibility = View.VISIBLE
                    holder.binding.tvSecondMainContentNotif.text = "project: ${item.projectName}"
                } else {
                    holder.binding.tvSecondMainContentNotif.visibility = View.GONE
                }
            }
        }

        holder.binding.tvDateStatusNotif.text = item.createdAt

    }

    override fun getItemCount(): Int {
        return notifMid.size
    }

    fun setListener(listNotifDataHistory: ListNotifDataHistory) {
        this.listNotifDataHistory = listNotifDataHistory
    }

    interface ListNotifDataHistory {
        fun onClickNotification(
            notificationHistoryId: Int,
            isRead: String,
            notificationType: String,
            relationId: Int
        )
    }
}




