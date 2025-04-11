package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.lowlevel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.ListHistoryPermission
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class HistoryPermissionAdapter(
    private val context: Context,
    var listHistoryPermission: ArrayList<ListHistoryPermission>
) : RecyclerView.Adapter<HistoryPermissionAdapter.ViewHolder>() {

    private lateinit var historyPermissionCallBack: HistoryPermissionCallBack
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.tv_namaList_permission)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_titleList_permission)
        var tvDate: TextView = itemView.findViewById(R.id.tv_dateList_permission)
        var tvShift: TextView = itemView.findViewById(R.id.tv_timeList_permission)
        var tvSubLoc: TextView = itemView.findViewById(R.id.tv_subLocationList_permission)
        var tvLoc: TextView = itemView.findViewById(R.id.tv_locationList_permission)
        var ivPermission: ImageView = itemView.findViewById(R.id.iv_list_permission)
        var ivClient: ImageView = itemView.findViewById(R.id.iv_clientList_permission)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_description_permission)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status_permission)
        var rlStatus: RelativeLayout = itemView.findViewById(R.id.rl_status_permission)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getPermission = listHistoryPermission[adapterPosition]
            historyPermissionCallBack.onClickedPermission(getPermission.permissionEmployeeId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_permission, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listHistoryPermission[position]
        val status = response.statusPermission

        holder.tvName.text = response.employeeName
        holder.tvTitle.text = response.title
        if (response.startDate == response.endDate){
            holder.tvDate.text = response.startDate
        } else {
            holder.tvDate.text = response.startDate + " - " + response.endDate
        }

        holder.tvDescription.text = response.description
        if (userLevel == "Operator") {
            holder.tvShift.visibility = View.VISIBLE
            holder.tvLoc.visibility = View.VISIBLE
            holder.tvShift.text = response.shiftDescription
            holder.tvLoc.text = response.locationName
            holder.tvSubLoc.text = response.subLocationName
        } else {
            holder.tvLoc.visibility = View.GONE
            holder.tvShift.visibility = View.GONE
            holder.tvSubLoc.text = response.shiftDescription
        }

        val url = context.getString(R.string.url)  + "assets.admin_master/images/permission_image/"
        context.let {
            Glide.with(it)
                .load(url + response.image)
                .apply(RequestOptions.fitCenterTransform())
                .into(holder.ivPermission)
        }

        val url2 = context.getString(R.string.url)  + "assets.admin_master/images/photo_profile/"
        context.let {
            Glide.with(it)
                .load(url2 + response.employeePhotoProfile)
                .apply(RequestOptions.fitCenterTransform())
                .into(holder.ivClient)
        }

        when(status) {
            "WAITING" -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.tvStatus.text = "Menunggu"
            }
            "REFUSE" -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.tvStatus.text = "Ditolak"
            }
            "ACCEPT" -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.tvStatus.text = "Diterima"
            }
            else -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.tvStatus.text = "Error"
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
        fun onClickedPermission(permissionId: Int)
    }
}