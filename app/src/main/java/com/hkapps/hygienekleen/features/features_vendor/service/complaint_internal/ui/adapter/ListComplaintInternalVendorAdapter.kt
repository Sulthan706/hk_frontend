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
import com.hkapps.hygienekleen.databinding.ListComplaintClientBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.datacomplaintinternal.Content

class ListComplaintInternalVendorAdapter(
    private val context: Context,
    var listComplaint: ArrayList<Content>) :
    RecyclerView.Adapter<ListComplaintInternalVendorAdapter.ViewHolder>() {

    private lateinit var listComplaintInternalCallback: ListComplaintInternalCallBack

    inner class ViewHolder(val binding: ListComplaintClientBinding) :
            RecyclerView.ViewHolder(binding.root), View.OnClickListener {

                init {
                    itemView.setOnClickListener(this)
                }

        override fun onClick(p0: View?) {
            val getComplaint = listComplaint[adapterPosition]
            listComplaintInternalCallback.onClickComplaint(getComplaint.complaintId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder((ListComplaintClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listComplaint[position]
        val status = response.statusComplaint

        // set user image
        val imgClient = response.createdByEmployeePhotoProfile
        val urlClient = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgClient"
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
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.binding.ivClientListComplaint)
        }

        holder.binding.tvNamaListComplaint.text = response.createdByEmployeeName
        holder.binding.tvTitleListComplaint.text = response.title
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
                holder.binding.tvDescriptionListComplaint.text = "Catatan: " + response.description
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_red)
                holder.binding.tvStatusHistoryComplaint.text = "Menunggu"
            }
            "ON PROGRESS" -> {
                holder.binding.tvDateListComplaint.text = response.date
                holder.binding.tvTimeListComplaint.text = response.time
                holder.binding.tvDescriptionListComplaint.text = "Balasan: " + response.comments
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.binding.tvStatusHistoryComplaint.text = "Dikerjakan"
            }
            "DONE" -> {
                if (response.doneAtDate != null){
                    holder.binding.tvDateListComplaint.text = "" + response.doneAtDate
                }else{
                    holder.binding.tvDateListComplaint.text = "-"
                }
                holder.binding.tvTimeListComplaint.text = ""
                holder.binding.tvDescriptionListComplaint.text = "Balasan: " + response.comments
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                holder.binding.tvStatusHistoryComplaint.text = "Selesai"
            }
            "CLOSE" -> {
                if (response.doneAtDate != null){
                    holder.binding.tvDateListComplaint.text = "" + response.doneAtDate
                }else{
                    holder.binding.tvDateListComplaint.text = "-"
                }
                holder.binding.tvTimeListComplaint.text = ""
                holder.binding.tvDescriptionListComplaint.text = "Balasan: " + response.comments
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.binding.tvStatusHistoryComplaint.text = "Tutup"
            }
            else -> {
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.binding.tvStatusHistoryComplaint.text = "Error"
            }
        }
    }

    override fun getItemCount(): Int {
        return listComplaint.size
    }

    fun setListener(listComplaintInternalCallback: ListComplaintInternalCallBack) {
        this.listComplaintInternalCallback = listComplaintInternalCallback
    }

    interface ListComplaintInternalCallBack {
        fun onClickComplaint(complaintId: Int)
    }
}