package com.hkapps.hygienekleen.features.features_client.notifcation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemNotificationClientBinding
import com.hkapps.hygienekleen.features.features_client.notifcation.model.DataArrayContent

class NotifClientAdapter(
    private var context: Context,
    var notificationsContent: ArrayList<DataArrayContent>
) : RecyclerView.Adapter<NotifClientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = ItemNotificationClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = notificationsContent[position]

        holder.binding.tvNameNotifClient.text = response.employeeName
        holder.binding.tvPlottingNotifClient.text = response.codePlottingArea
        holder.binding.tvDateNotifClient.text = response.dayDate
        holder.binding.tvShiftNotifClient.text = response.shiftDescription
        holder.binding.tvAreaNotifClient.text = response.locationName
        holder.binding.tvSubareaNotifClient.text = response.subLocationName

        val img = response.employeePhotoProfile
        val url =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivNotifClient.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivNotifClient)
        }
    }

    override fun getItemCount(): Int {
        return notificationsContent.size
    }

    inner class ViewHolder(val binding: ItemNotificationClientBinding) : RecyclerView.ViewHolder(binding.root) {

    }

//    fun setListener(notificationCallback: NotificationCallback) {
//        this.notificationCallback = notificationCallback
//    }
//
//    interface NotificationCallback {
//        fun onClickNotification(notificationId: Int, targetId: String, position: Int, type: String)
//    }
}