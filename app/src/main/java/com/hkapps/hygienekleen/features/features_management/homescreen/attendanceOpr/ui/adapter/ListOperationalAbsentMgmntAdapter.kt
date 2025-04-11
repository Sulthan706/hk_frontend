package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

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
import com.hkapps.hygienekleen.databinding.ListCountAbsentOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.Data

class ListOperationalAbsentMgmntAdapter(
    private var context: Context,
    var listOperational: ArrayList<Data>
): RecyclerView.Adapter<ListOperationalAbsentMgmntAdapter.ViewHolder>() {

    private lateinit var reqContext: Context
    private lateinit var listOperationalCallBack: ListOperationalCallBack

    inner class ViewHolder(val binding: ListCountAbsentOperationalBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getOperational = listOperational[adapterPosition]
            listOperationalCallBack.onClickOperational(getOperational.employeeId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        reqContext = parent.context
        return ViewHolder(ListCountAbsentOperationalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperational[position]

        holder.binding.tvCountAbsentOpr.text = response.employeeName
        holder.binding.tvCountHadir.text = response.hadirCount.toString()
        holder.binding.tvCountAlfa.text = response.tidakHadirCount.toString()
        holder.binding.tvCountIzin.text = response.izinCount.toString()
        holder.binding.tvCountLemburGanti.text = response.lemburGantiCount.toString()
//        holder.binding.tvCountLemburTagih.text = response.tidakHadirCount.toString()
        holder.binding.tvProgressBarCountAbsentOpr.text = "${response.totalAttendanceInPercent}%"
        holder.binding.progressBarCountAbsentOpr.progress = response.totalAttendanceInPercent

        // set photo employee
        val img = response.employeePhotoProfile
        val url =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivCountAbsentOpr.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(context.resources.getDrawable(R.drawable.ic_error_image))

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivCountAbsentOpr)
        }
    }

    override fun getItemCount(): Int {
        return listOperational.size
    }

    fun setListener(listOperationalCallBack: ListOperationalCallBack) {
        this.listOperationalCallBack = listOperationalCallBack
    }

    interface ListOperationalCallBack {
        fun onClickOperational(employeeId: Int)
    }
}