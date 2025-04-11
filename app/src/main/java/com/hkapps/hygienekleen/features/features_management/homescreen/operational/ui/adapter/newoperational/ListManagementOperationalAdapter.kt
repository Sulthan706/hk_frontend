package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListAllOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listmanagement.ContentManagementOperational

class ListManagementOperationalAdapter(
    private var context: Context,
    var listOperational: ArrayList<ContentManagementOperational>
): RecyclerView.Adapter<ListManagementOperationalAdapter.ViewHolder>() {

    private lateinit var listManagementOperationalCallback: ListManagementOperationalCallback



    inner class ViewHolder(val binding: ListAllOperationalBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val response = listOperational[adapterPosition]
            listManagementOperationalCallback.onClickManagement(response.adminMasterId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListAllOperationalBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listOperational.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperational[position]

        holder.binding.tvNameOperational.text = response.adminMasterName
        holder.binding.tvJobOperational.text = response.adminMasterJabatan
        holder.binding.tvProjectOperational.text = response.levelJabatan

        //set user image
        val img = response.adminMasterImage
        val url =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivImageOperational.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivImageOperational)
        }
    }

    fun setListener(listManagementOperationalCallback: ListManagementOperationalCallback){
        this.listManagementOperationalCallback = listManagementOperationalCallback
    }

    interface ListManagementOperationalCallback {
        fun onClickManagement(adminMasterId: Int)
    }


}