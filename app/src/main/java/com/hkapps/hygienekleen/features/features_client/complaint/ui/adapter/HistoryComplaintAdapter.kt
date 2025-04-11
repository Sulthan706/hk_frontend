package com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_client.complaint.model.historyComplaint.ListHistoryComplaint

class HistoryComplaintAdapter(
    private val context: Context,
    var listHistoryComplaint: ArrayList<ListHistoryComplaint>
) : RecyclerView.Adapter<HistoryComplaintAdapter.ViewHolder>() {

    private lateinit var historyComplaintCallBack: HistoryComplaintCallBack

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var ivClient: ImageView = itemView.findViewById(R.id.iv_clientListComplaint)
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_complaint_client, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listHistoryComplaint[position]
        val status = response.statusComplaint

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
            holder.ivClient.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.ivClient)
        }
        holder.tvName.text = if (response.clientName == null || response.clientName == "" || response.clientName == "null") {
            response.createdByEmployeeName
        } else {
            response.clientName
        }
        holder.tvTitle.text = response.title
        holder.tvSubLoc.text = response.subLocationName
        holder.tvLoc.text = response.locationName
        holder.tvDescription.text = response.description

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
            .skipMemoryCache(true)
            .centerCrop()
            .error(R.drawable.ic_error_image)

        Glide.with(context)
            .load(context.getString(R.string.url) + "assets.admin_master/images/complaint/${response.image}")
            .apply(requestOptions)
            .into(holder.ivComplaint)

        when (status) {
            "WAITING" -> {
                holder.tvDate.text = response.date
                holder.tvTime.text = response.time
                holder.tvDescription.text = "Catatan klien: " + response.description
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.tvStatus.text = "Menunggu"
            }
            "ON PROGRESS" -> {
                holder.tvDate.text = response.date
                holder.tvTime.text = response.time
                holder.tvDescription.text = "Balasan pengawas: " + response.comments
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.tvStatus.text = "Dikerjakan"
            }
            "DONE" -> {
                if (response.doneAtDate != null){
                    holder.tvDate.text = "" + response.doneAtDate
                }else{
                    holder.tvDate.text = "-"
                }
                holder.tvTime.text = ""
                holder.tvDescription.text = "Balasan pengawas: " + response.comments
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                holder.tvStatus.text = "Selesai"
            }
            "CLOSE" -> {
                if (response.doneAtDate != null){
                    holder.tvDate.text = "" + response.doneAtDate
                }else{
                    holder.tvDate.text = "-"
                }
                holder.tvTime.text = ""
                holder.tvDescription.text = "Balasan pengawas: " + response.comments
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.tvStatus.text = "Tutup"
            }
            else -> {
                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
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