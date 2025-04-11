package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemNotificationBinding
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.DataArrayContent

class NotifVendorAdapter(
    private var context: Context,
    var notificationsContent: ArrayList<DataArrayContent>
) :
    RecyclerView.Adapter<NotifVendorAdapter.ViewHolder>() {
    private lateinit var notificationCallback: NotificationCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = notificationsContent[position]
        val status = response.statusComplaint

        holder.binding.tvClientNameNotif.text = response.clientName

        if (response.clientName == "" || response.clientName == null){
            holder.binding.tvClientNameNotif.text = response.createdByEmployeeName
        }else if (response.createdByEmployeeName == "" || response.createdByEmployeeName == null){
            holder.binding.tvClientNameNotif.text = response.clientName
        }

        // set user image
        val imgClient = response.clientPhotoProfile
        val urlClient =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgClient"

        if (imgClient == "null" || imgClient == null || imgClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivClientNotif.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.binding.ivClientNotif)
        }

        holder.binding.tvTitleNotif.text = response.title
        holder.binding.tvDescriptionNotif.text = response.description

        holder.binding.tvLocationNotif.text = response.locationName
        holder.binding.tvSubLocationNotif.text = response.subLocationName

        val url = context!!.getString(R.string.url) +"assets.admin_master/images/complaint/"
        context.let {
            it?.let { it1 ->
                Glide.with(it1)
                    .load(url + response.image)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.binding.ivComplaintNotif)
            }
        }

//                     set photo profile
        val urls = context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/"
        context.let {
            it?.let { it1 ->
                if (response.clientPhotoProfile == "null" ||response.clientPhotoProfile == null || response.clientPhotoProfile == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imageResource =
                        it.resources.getIdentifier(uri, null, it.packageName)
                    val res = it.getDrawable(imageResource)
                    holder.binding.ivClientNotif.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                    Glide.with(it1)
                        .load(urls + response.clientPhotoProfile)
                        .apply(requestOptions)
                        .into(holder.binding.ivClientNotif)
                }
            }
        }

        when(status) {
            "WAITING" -> {
                holder.binding.tvDateNotif.text = response.date
                holder.binding.tvTimeNotif.text = response.time
                holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.binding.tvStatusComplaintNotif.text = "Mengunggu"
            }
            "ON PROGRESS" -> {
                holder.binding.tvDateNotif.text = response.date
                holder.binding.tvTimeNotif.text = response.time
                holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.binding.tvStatusComplaintNotif.text = "Dikerjakan"
            }
            "DONE" -> {
                if (response.doneAtDate != null){
                    holder.binding.tvDateNotif.text = "" + response.doneAtDate
                }else{
                    holder.binding.tvDateNotif.text= "-"
                }
                holder.binding.tvTimeNotif.text = ""
                holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                holder.binding.tvStatusComplaintNotif.text = "Selesai"
            }
            "CLOSE" -> {
                if (response.doneAtDate != null){
                    holder.binding.tvDateNotif.text = "" + response.doneAtDate
                }else{
                    holder.binding.tvDateNotif.text= "-"
                }
                holder.binding.tvTimeNotif.text = ""
                holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.binding.tvStatusComplaintNotif.text = "Tutup"
            }
            else -> {
                holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.binding.tvStatusComplaintNotif.text = "Error"
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationsContent.size
    }

    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var position = adapterPosition
            var notification = notificationsContent[position]
            notificationCallback.onClickNotification(
                notification.clientId,
                notification.projectId,
                position,
                notification.title,
                notification.complaintId
            )
        }
    }

    fun setListener(notificationCallback: NotificationCallback) {
        this.notificationCallback = notificationCallback
    }

    interface NotificationCallback {
        fun onClickNotification(notificationId: Int, targetId: String, position: Int, type: String, complaintId: Int)
    }
}