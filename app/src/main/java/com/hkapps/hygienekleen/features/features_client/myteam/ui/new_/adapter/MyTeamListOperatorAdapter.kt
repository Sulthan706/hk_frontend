package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListPengawasMyteamBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamscheduleteam.Operator

class MyTeamListOperatorAdapter(
    private val context: Context,
    var listOperatorMyTeam: ArrayList<Operator>):
RecyclerView.Adapter<MyTeamListOperatorAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ListPengawasMyteamBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListPengawasMyteamBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperatorMyTeam[position]
        holder.binding.tvNameEmployeeHistoryAttendanceProject.text = response.employeeName
        // set photo profile
        val imgEmployee = response.employeeImage
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgEmployee"

        if (imgEmployee == "null" || imgEmployee == null || imgEmployee == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.getDrawable(imaResource)
            holder.binding.ivEmployeeHistoryAttendanceProject.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(context.getDrawable(R.drawable.ic_error_image))

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivEmployeeHistoryAttendanceProject)
        }
        holder.binding.tvStatusAbsentEmployeeHistoryAttendanceProject.text = "${response.employeeNuc} | ${response.jobCode}"
    }

    override fun getItemCount(): Int {
        return listOperatorMyTeam.size
    }
}