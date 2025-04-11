package com.hkapps.hygienekleen.features.features_management.service.permission.ui.adapter

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
import com.hkapps.hygienekleen.databinding.ListPermissionBinding
import com.hkapps.hygienekleen.features.features_management.service.permission.model.historyPermissionManagement.Content

class HistoryPermissionManagementAdapter(
    private val context: Context,
    var listHistoryPermission: ArrayList<Content>
) : RecyclerView.Adapter<HistoryPermissionManagementAdapter.ViewHolder>() {

    private lateinit var historyPermissionCallBack: HistoryPermissionCallBack

    inner class ViewHolder(val binding: ListPermissionBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selected = listHistoryPermission[adapterPosition]
            historyPermissionCallBack.onClickPermission(selected.permissionEmployeeId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListPermissionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listHistoryPermission[position]
        val status = response.statusPermission

        // set layout not use
        holder.binding.tvDateListPermission.visibility = View.GONE
        holder.binding.tvSubLocationListPermission.visibility = View.GONE

        // set data
        holder.binding.tvNamaListPermission.text = response.employeeName
        holder.binding.tvTitleListPermission.text = response.title
        holder.binding.tvTimeListPermission.text = response.startDate
        holder.binding.tvLocationListPermission.text = response.endDate
        holder.binding.tvDescriptionPermission.text = response.description

        // set profile picture
        val img = response.employeePhotoProfile
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == "" || img == null) {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivClientListPermission.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(context.resources.getDrawable(R.drawable.ic_error_image))

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivClientListPermission)
        }

        // set image permission
        val url2 = context.getString(R.string.url)  + "assets.admin_master/images/permission_image/"
        context.let {
            Glide.with(it)
                .load(url2 + response.image)
                .apply(RequestOptions.fitCenterTransform())
                .into(holder.binding.ivListPermission)
        }

        when(status) {
            "WAITING" -> {
                holder.binding.rlStatusPermission.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.binding.tvStatusPermission.text = "Menunggu"
            }
            "REFUSE" -> {
                holder.binding.rlStatusPermission.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.binding.tvStatusPermission.text = "Ditolak"
            }
            "ACCEPT" -> {
                holder.binding.rlStatusPermission.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.binding.tvStatusPermission.text = "Diterima"
            }
            else -> {
                holder.binding.rlStatusPermission.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.binding.tvStatusPermission.text = "Error"
            }
        }
    }

    override fun getItemCount(): Int {
        return listHistoryPermission.size
    }

    fun setListener(historyPermissionCallBack: HistoryPermissionCallBack) {
        this.historyPermissionCallBack = historyPermissionCallBack
    }

    interface HistoryPermissionCallBack {
        fun onClickPermission(permissionId: Int)
    }
}