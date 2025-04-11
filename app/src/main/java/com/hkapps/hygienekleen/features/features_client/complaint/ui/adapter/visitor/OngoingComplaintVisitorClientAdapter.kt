package com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListComplaintClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.model.complaintvisitorclient.ContentCtalkVisitorClient

class OngoingComplaintVisitorClientAdapter
    (var context: Context, var contentCtalkVisitorClients: ArrayList<ContentCtalkVisitorClient>):
RecyclerView.Adapter<OngoingComplaintVisitorClientAdapter.ViewHolder>() {

    private lateinit var ctalkOngoingVisitorClientCallBack: CtalkOngoingVisitorCallBack

    inner class ViewHolder(val binding: ListComplaintClientBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val getComplaint = contentCtalkVisitorClients[adapterPosition]
            ctalkOngoingVisitorClientCallBack.onClickOngoingComplaintVisitor(getComplaint.complaintId)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListComplaintClientBinding.inflate(
            LayoutInflater.from(parent.context),parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = contentCtalkVisitorClients[position]

        holder.binding.tvNamaListComplaintVisitor.text = if (item.clientName == null || item.clientName == "" || item.clientName == "null") {
            item.createdByEmployeeName
        } else {
            item.clientName
        }
        holder.binding.tvTitleListComplaint.text = item.title.ifEmpty { "" }
        holder.binding.tvSubLocationListComplaint.text = item.subLocationName.ifEmpty { "" }
        holder.binding.tvLocationListComplaint.text = item.locationName.ifEmpty { "" }
        holder.binding.tvDescriptionListComplaint.text = item.description.ifEmpty { "" }

        setUserImage(item.clientPhotoProfile, holder.binding.ivClientListComplaint)

        when (item.statusComplaint) {
            "WAITING" -> {
                holder.binding.tvDateListComplaint.text = item.date
                holder.binding.tvTimeListComplaint.text = item.time
                holder.binding.tvDescriptionListComplaint.text = "Catatan klien: " + item.description
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_red)
                holder.binding.tvStatusHistoryComplaint.text = "Menunggu"
            }
            "ON PROGRESS" -> {
                holder.binding.tvDateListComplaint.text = item.date
                holder.binding.tvTimeListComplaint.text = item.time
                holder.binding.tvDescriptionListComplaint.text = "Catatan klien: " + item.description
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                holder.binding.tvStatusHistoryComplaint.text = "Dikerjakan"
            }
            "DONE" -> {
                if (item.doneAtDate != null){
                    holder.binding.tvDateListComplaint.text = "" + item.doneAtDate
                }else{
                    holder.binding.tvDateListComplaint.text = "-"
                }
                holder.binding.tvTimeListComplaint.text = item.time
                holder.binding.tvDescriptionListComplaint.text = "Balasan pengawas: " + item.comments
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                holder.binding.tvStatusHistoryComplaint.text = "Selesai"
            }
            "CLOSE" -> {
                if (item.doneAtDate != null){
                    holder.binding.tvDateListComplaint.text = "" + item.doneAtDate
                }else{
                    holder.binding.tvDateListComplaint.text = "-"
                }
                holder.binding.tvTimeListComplaint.text = item.time
                holder.binding.tvDescriptionListComplaint.text = item.comments
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                holder.binding.tvStatusHistoryComplaint.text = "Tutup"
            }
            else -> {
                holder.binding.rlStatusHistoryComplaint.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                holder.binding.tvStatusHistoryComplaint.text = "Error"
            }
        }
    }

    private fun setUserImage(item: String, place: ImageView){
        // set user image
        val urlClient =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$item"

        if (item == "null" || item == null || item == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            place.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(place)
        }
    }

    override fun getItemCount(): Int {
        return contentCtalkVisitorClients.size
    }

    fun setListener(ctalkOngoingVisitorCallback: CtalkOngoingVisitorCallBack){
        this.ctalkOngoingVisitorClientCallBack = ctalkOngoingVisitorCallback
    }

    interface CtalkOngoingVisitorCallBack {
        fun onClickOngoingComplaintVisitor(complaintId: Int)

    }

}


