package com.hkapps.hygienekleen.features.features_client.visitreport.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemHistoryVisitReportBinding
import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.Content

class ListHistoryVisitAdapter(
    private val context: Context,
    var listHistoryVisit: ArrayList<Content>

):RecyclerView.Adapter<ListHistoryVisitAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemHistoryVisitReportBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
           ItemHistoryVisitReportBinding.inflate(
               LayoutInflater.from(parent.context),
               parent, false
           )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listHistoryVisit[position]
        holder.binding.tvMyTeamEmployeeNameMgmnt.text = response.adminMasterName
        holder.binding.tvVisitReportIn.text = response.checkIn
        holder.binding.tvVisitReportOut.text = response.checkOut

        // set photo profile
        val imgEmployee = response.adminMasterImage
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgEmployee"

        if (imgEmployee == "null" || imgEmployee == null || imgEmployee == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.getDrawable(imaResource)
            holder.binding.ivMyTeamEmployee.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(context.getDrawable(R.drawable.ic_error_image))

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivMyTeamEmployee)
        }
    }

    override fun getItemCount(): Int {
        return listHistoryVisit.size
    }
}