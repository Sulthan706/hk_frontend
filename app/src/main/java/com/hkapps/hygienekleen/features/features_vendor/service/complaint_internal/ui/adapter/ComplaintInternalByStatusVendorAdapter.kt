package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListComplaintInternalBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.datacomplaintinternal.Content

class ComplaintInternalByStatusVendorAdapter(
    private val context: Context,
    var listComplaint: ArrayList<Content>,
    val statusComplaint: String) :
    RecyclerView.Adapter<ComplaintInternalByStatusVendorAdapter.ViewHolder>() {

    private lateinit var complaintStatusCallback: ComplaintStatusCallback
    private var status: String = ""

    inner class ViewHolder(val binding: ListComplaintInternalBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getComplaint = listComplaint[adapterPosition]
            complaintStatusCallback.onClickComplaint(getComplaint.complaintId)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ListComplaintInternalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.clListComplaintClient.visibility = View.VISIBLE
        val response = listComplaint[position]
        status = response.statusComplaint
        if (status == statusComplaint) {
            //set user image
            val imgClient = response.createdByEmployeePhotoProfile
            val urlClient =
                context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgClient"

            if (imgClient == "null" || imgClient == null || imgClient == "") {
                val uri =
                    "@drawable/profile_default" // where myresource (without the extension) is the file
                val imaResource =
                    context.resources.getIdentifier(uri, null, context.packageName)
                val res = context.resources.getDrawable(imaResource)
                holder.binding.ivClientListComplaint.setImageDrawable(res)
            } else {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)

                Glide.with(context)
                    .load(urlClient)
                    .apply(requestOptions)
                    .into(holder.binding.ivClientListComplaint)
            }

            holder.binding.tvNamaListComplaint.text = response.createdByEmployeeName
            holder.binding.tvTitleListComplaint.text = response.title
            holder.binding.tvDateListComplaint.text = response.date
            holder.binding.tvTimeListComplaint.text = response.time
            holder.binding.tvSubLocationListComplaint.text = response.subLocationName
            holder.binding.tvLocationListComplaint.text = response.locationName
            holder.binding.tvDescriptionListComplaint.text = response.description
            val url = context.getString(R.string.url) + "assets.admin_master/images/complaint/"
            context.let {
                Glide.with(it)
                    .load(url + response.image)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.binding.ivListComplaint)
            }

            when (status) {
                "WAITING" -> {
                    holder.binding.tvDateListComplaint.text = response.date
                    holder.binding.tvTimeListComplaint.text = response.time
                    holder.binding.tvDescriptionListComplaint.text =
                        "Catatan klien: " + response.description
                    holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                    holder.binding.tvStatusHistoryComplaint.text = "Menunggu"
                }
                "ON PROGRESS" -> {
                    holder.binding.tvDateListComplaint.text = response.date
                    holder.binding.tvTimeListComplaint.text = response.time
                    holder.binding.tvDescriptionListComplaint.text =
                        "Balasan pengawas: " + response.comments
                    holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                    holder.binding.tvStatusHistoryComplaint.text = "Dikerjakan"
                }
                "DONE" -> {
                    if (response.doneAtDate != null) {
                        holder.binding.tvDateListComplaint.text = "" + response.doneAtDate
                    } else {
                        holder.binding.tvDateListComplaint.text = "-"
                    }
                    holder.binding.tvTimeListComplaint.text = ""
                    holder.binding.tvDescriptionListComplaint.text =
                        "Balasan pengawas: " + response.comments
                    holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                    holder.binding.tvStatusHistoryComplaint.text = "Selesai"
                }
                "CLOSE" -> {
                    if (response.doneAtDate != null) {
                        holder.binding.tvDateListComplaint.text = "-" + response.doneAtDate
                    } else {
                        holder.binding.tvDateListComplaint.text = ""
                    }
                    holder.binding.tvTimeListComplaint.text = ""
                    holder.binding.tvDescriptionListComplaint.text =
                        "Balasan pengawas: " + response.comments
                    holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                    holder.binding.tvStatusHistoryComplaint.text = "Tutup"
                }
                else -> {
                    holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                    holder.binding.tvStatusHistoryComplaint.text = "Error"
                }
            }

        } else {
            holder.binding.clListComplaintClient.visibility = View.GONE
            holder.binding.clListComplaintClient.layoutParams = ViewGroup.LayoutParams(0, 0)
        }
    }

    override fun getItemCount(): Int {
        return listComplaint.size
    }

    fun setListener(complaintStatusCallback: ComplaintStatusCallback) {
        this.complaintStatusCallback = complaintStatusCallback
    }

    interface ComplaintStatusCallback {
        fun onClickComplaint(complaintId: Int)
    }

}