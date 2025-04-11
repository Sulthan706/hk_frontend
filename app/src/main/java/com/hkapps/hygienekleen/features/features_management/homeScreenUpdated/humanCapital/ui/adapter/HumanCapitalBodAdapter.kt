package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter

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
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.Content

class HumanCapitalBodAdapter(
    private var context: Context,
    var listManPower: ArrayList<Content>
): RecyclerView.Adapter<HumanCapitalBodAdapter.ViewHolder>() {

    private lateinit var humanCapitalBodCallback: HumanCapitalBodCallback

    inner class ViewHolder(val binding: ListAllOperationalBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getEmployee = listManPower[adapterPosition]
            humanCapitalBodCallback.onClickMpBod(getEmployee.employeeId, getEmployee.codeProject)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListAllOperationalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listManPower.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listManPower[position]

        // set user data
        holder.binding.tvNameOperational.text = response.employeeName
        holder.binding.tvJobOperational.text = response.employeeJob
        holder.binding.tvProjectOperational.text = response.projectName

        // set user image
        val img = response.employeeImage
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
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

    fun setListener(humanCapitalBodCallback: HumanCapitalBodCallback) {
        this.humanCapitalBodCallback = humanCapitalBodCallback
    }

    interface HumanCapitalBodCallback {
        fun onClickMpBod(userId: Int, projectCode: String)
    }
}