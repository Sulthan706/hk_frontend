package com.hkapps.hygienekleen.features.features_management.shareloc.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemListAllManagementBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.Management
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.bod.ListManagementLocationActivity

class ListAllManagementLocationAdapter(private val context: Context, var listALlManagement: ArrayList<Management>):
RecyclerView.Adapter<ListAllManagementLocationAdapter.ViewHolder>(){

    var selectedItem = -1
    private lateinit var clickManagement: ClickManagement

    inner class ViewHolder(val binding: ItemListAllManagementBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val clickManagements = listALlManagement[adapterPosition]
            clickManagement.onClickManagement(
                clickManagements.managementId,
                clickManagements.managementName
            )
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListAllManagementBinding.inflate(LayoutInflater.from(
                parent.context
            ), parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemManagement = listALlManagement[position]

        holder.binding.tvNameManagementLocation.text = if (itemManagement.managementName.isNullOrEmpty()) "No data"
        else itemManagement.managementName
        holder.binding.tvDateManagementLocation.text = if (itemManagement.updated_at.isNullOrEmpty()) "No data"
        else itemManagement.updated_at
        holder.binding.tvAddressManagementLocation.text = if (itemManagement.address.isNullOrEmpty()) "No data"
        else itemManagement.address
        Log.d("agri","${itemManagement.address}")

        // set user image
        val imgClient = itemManagement.managementImage
        val urlClient =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgClient"

        if (imgClient == "null" || imgClient == null || imgClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivImageUserManagement.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.binding.ivImageUserManagement)
        }

    }

    override fun getItemCount(): Int {
        return listALlManagement.size
    }

    fun setListener(listManagementLocationActivity: ListManagementLocationActivity){
        this.clickManagement = listManagementLocationActivity
    }

    interface ClickManagement {
        fun onClickManagement(managementId: Int, managementName: String)
    }


}