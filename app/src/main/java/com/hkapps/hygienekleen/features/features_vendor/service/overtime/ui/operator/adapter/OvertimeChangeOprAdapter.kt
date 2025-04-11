package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListOvertimeChangeBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.ListOvertimeChangeResponse.Content

class OvertimeChangeOprAdapter(
    private var context: Context,
    var listOvertime: ArrayList<Content>
) : RecyclerView.Adapter<OvertimeChangeOprAdapter.ViewHolder>() {

    private lateinit var listOvertimeChangeCallBack: ListOvertimeChangeCallBack

    inner class ViewHolder(val binding: ListOvertimeChangeBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = listOvertime[adapterPosition]
            listOvertimeChangeCallBack.onClickOvertime(position.overtimeId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ListOvertimeChangeBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOvertime[position]

        holder.binding.tvTitleListOvertimeChange.text = response.title
        holder.binding.tvNamaListOvertimeChange.text = response.employeeNameCreated
        holder.binding.tvDateListOvertimeChange.text = response.atDate
        holder.binding.tvTimeListOvertimeChange.text = response.shiftDescription
        holder.binding.tvLocListOvertimeChange.text = response.locationName
        holder.binding.tvSubLocListOvertimeChange.text = response.subLocationName

        // set user images
        val img = response.employeePhotoProfileCreated
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource = context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivListOvertimeChange.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivListOvertimeChange)
        }
    }

    override fun getItemCount(): Int {
        return listOvertime.size
    }

    fun setListener (listOvertimeChangeCallBack: ListOvertimeChangeCallBack) {
        this.listOvertimeChangeCallBack = listOvertimeChangeCallBack
    }

    interface ListOvertimeChangeCallBack {
        fun onClickOvertime(overtimeId: Int)
    }
}