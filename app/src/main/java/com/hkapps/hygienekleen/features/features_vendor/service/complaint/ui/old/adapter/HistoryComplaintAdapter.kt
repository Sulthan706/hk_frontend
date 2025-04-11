package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.old.adapter

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
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.ListHistoryComplaint

class HistoryComplaintAdapter(
    private val context: Context,
    var listHistoryComplaint: ArrayList<ListHistoryComplaint>
) : RecyclerView.Adapter<HistoryComplaintAdapter.ViewHolder>() {

    private lateinit var historyComplaintCallBack: HistoryComplaintCallBack

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.tv_namaListComplaint)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_titleListComplaint)
        var tvDate: TextView = itemView.findViewById(R.id.tv_dateListComplaint)
        var tvTime: TextView = itemView.findViewById(R.id.tv_timeListComplaint)
        var tvSubLoc: TextView = itemView.findViewById(R.id.tv_subLocationListComplaint)
        var tvLoc: TextView = itemView.findViewById(R.id.tv_locationListComplaint)
        var ivComplaint: ImageView = itemView.findViewById(R.id.iv_listComplaint)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_descriptionListComplaint)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_statusHistoryComplaint)
        var rlStatus: RelativeLayout = itemView.findViewById(R.id.rl_statusHistoryComplaint)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getComplaint = listHistoryComplaint[adapterPosition]
            historyComplaintCallBack.onClickedComplaint(getComplaint.complaintId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_complaint_client, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listHistoryComplaint[position]
        val status = response.statusComplaint

        if (response.clientName == "" || response.clientName == null){
            holder.tvName.text = response.createdByEmployeeName
        }else if (response.createdByEmployeeName == "" || response.createdByEmployeeName == null){
            holder.tvName.text = response.clientName
        }

        holder.tvTitle.text = response.title
        holder.tvDate.text = response.date
        holder.tvTime.text = response.time
        holder.tvSubLoc.text = response.subLocationName
        holder.tvLoc.text = response.locationName
        holder.tvDescription.text = response.description
        val url = context.getString(R.string.url) +"assets.admin_master/images/complaint/"
        context.let {
            Glide.with(it)
                .load(url + response.image)
                .apply(RequestOptions.fitCenterTransform())
                .into(holder.ivComplaint)
        }

        when(status) {
            "WAITING" -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.tvStatus.text = "Menunggu"
            }
            "ON PROGRESS" -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.tvStatus.text = "Dikerjakan"
            }
            "DONE" -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.tvStatus.text = "Selesai"
            }
            else -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.tvStatus.text = "Error"
            }
        }
    }

    override fun getItemCount(): Int {
        return listHistoryComplaint.size
    }

    fun setListener(historyComplaintCallBack: HistoryComplaintCallBack) {
        this.historyComplaintCallBack = historyComplaintCallBack
    }

    interface HistoryComplaintCallBack {
        fun onClickedComplaint(complaintId: Int)
    }
}