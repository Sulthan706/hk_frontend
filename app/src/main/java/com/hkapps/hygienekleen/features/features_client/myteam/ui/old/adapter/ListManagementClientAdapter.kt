package com.hkapps.hygienekleen.features.features_client.myteam.ui.old.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListLeaderMyteamBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.DataManagementStructuralClientModel

class ListManagementClientAdapter(
    private var context: Context,
    var listManagement: ArrayList<DataManagementStructuralClientModel>
) : RecyclerView.Adapter<ListManagementClientAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: ListLeaderMyteamBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListLeaderMyteamBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listManagement[position]

        holder.binding.llCountBelumAbsen.visibility = View.GONE
        holder.binding.tvNameLeaderMyTeam.text = response.adminMasterName
        holder.binding.tvJobLeaderMyTeam.text = response.levelJabatan

        // set image
        val img = response.adminMasterImage
        val url = context.getString(R.string.url) + "assets.admin_master/admin/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivImageLeaderMyteam.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivImageLeaderMyteam)
        }
    }

    override fun getItemCount(): Int {
        return listManagement.size
    }


}