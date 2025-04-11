package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListAllManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalbyprojectcode.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel

class ListOperationalProfileProjectAdapter(
    private var context: Context,
    var listOperational: ArrayList<Content>,
    private val viewModel: OperationalManagementViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ListOperationalProfileProjectAdapter.ViewHolder>() {

    private lateinit var listOperationalProfileProjectCallback: ListOperationalProfileProjectCallback

    inner class ViewHolder(val binding: ListAllManagementBinding):
    RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val response = listOperational[adapterPosition]
            listOperationalProfileProjectCallback.onClick(response.idEmployee, response.employeeName)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(ListAllManagementBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperational[position]

        holder.binding.tvNameOperational.text = response.employeeName
        holder.binding.tvJobOperational.text = response.employeeCode

        // set user image
        val img = response.employeePhotoProfile
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

    override fun getItemCount(): Int {
        return listOperational.size
    }

    fun setListener(listOperationalProfileProjectCallback: ListOperationalProfileProjectCallback){
        this.listOperationalProfileProjectCallback = listOperationalProfileProjectCallback
    }

    interface ListOperationalProfileProjectCallback{
        fun onClick(idEmployee: Int, employeeName: String)
    }
}