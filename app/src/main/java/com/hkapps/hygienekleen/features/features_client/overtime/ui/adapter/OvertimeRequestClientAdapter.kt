package com.hkapps.hygienekleen.features.features_client.overtime.ui.adapter

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
import com.hkapps.hygienekleen.databinding.ListOvertimeChangeBinding
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeReqClient.Content


class OvertimeRequestClientAdapter(
    private var context: Context,
    var listOvertime: ArrayList<Content>
) : RecyclerView.Adapter<OvertimeRequestClientAdapter.ViewHolder>() {

    private lateinit var listOvertimeRequestCallback: ListOvertimeReqCallback

    inner class ViewHolder(val binding: ListOvertimeChangeBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = listOvertime[adapterPosition]
            listOvertimeRequestCallback.onClickItem(position.overtimeTagihId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ListOvertimeChangeBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOvertime[position]

        holder.binding.tvNamaListOvertimeChange.text = response.createdByName
        holder.binding.tvTitleListOvertimeChange.text = response.title
        holder.binding.tvDateListOvertimeChange.text = response.atDate
        holder.binding.tvTimeListOvertimeChange.text = "${response.startAt} - ${response.endAt}"
        holder.binding.tvLocListOvertimeChange.text = response.locationName
        holder.binding.tvSubLocListOvertimeChange.text = response.subLocationName

        // set user images
        val img = response.image
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

    fun setListener(listOvertiReqCallback: ListOvertimeReqCallback) {
        this.listOvertimeRequestCallback = listOvertiReqCallback
    }

    interface ListOvertimeReqCallback {
        fun onClickItem(overtimeId: Int)
    }
}