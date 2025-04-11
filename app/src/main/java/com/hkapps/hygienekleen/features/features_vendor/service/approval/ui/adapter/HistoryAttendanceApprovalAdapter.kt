package com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListHistoryApprovalBinding
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.Content

class HistoryAttendanceApprovalAdapter(
    private val context: Context,
    var listHistory: ArrayList<Content>
): RecyclerView.Adapter<HistoryAttendanceApprovalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListHistoryApprovalBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListHistoryApprovalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listHistory[position]

        // set user image
        val imgClient = response.employeeImages
        val urlClient = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgClient"
        if (imgClient == "null" || imgClient == null || imgClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivHistoryApproval.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.binding.ivHistoryApproval)
        }

        // set data
        holder.binding.tvNamaHistoryApproval.text = response.employeeName
        holder.binding.tvNucJobHistoryApproval.text = "${response.employeeCode} | ${response.jobCode}"
        holder.binding.tvShiftHistoryApproval.text = response.shift
        holder.binding.tvTimeHistoryApproval.text = "${response.shiftStartAt} - ${response.shiftEndAt}"
        holder.binding.tvTimeCreatedHistoryApproval.text = response.createdAt
        holder.binding.tvStatusHistoryApproval.text = when(response.status) {
            "APPROVED" -> "Telah disetujui"
            "REFUSE" -> "Ditolak"
            else -> response.status
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

}