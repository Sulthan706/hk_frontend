package com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemCtalkVisitorBinding
import com.hkapps.hygienekleen.features.features_client.complaint.model.detailHistoryComplaint.VisitorObjectClient


class VisitorObjectClientAdapter(
    var context: Context,
    var visitorObjects: ArrayList<VisitorObjectClient>
): RecyclerView.Adapter<VisitorObjectClientAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCtalkVisitorBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCtalkVisitorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = visitorObjects[position]
        val count = position + 1
        holder.binding.tvObjectName.text = "objek $count"
        holder.binding.tvDetailObjectName.text = item.namaObject.ifEmpty { "" }

        if (item.file.isNullOrEmpty()){
            holder.binding.ivBeforeVisitorImage.visibility = View.GONE
        } else {
            holder.binding.ivBeforeVisitorImage.visibility = View.VISIBLE
        }

        if (item.fileTwo.isNullOrEmpty()){
            holder.binding.ivAfterVisitorImage.visibility = View.GONE
        } else {
            holder.binding.ivAfterVisitorImage.visibility = View.VISIBLE
        }

        setImages(item.file, holder.binding.ivBeforeVisitorImage)
        setImages(item.fileTwo, holder.binding.ivAfterVisitorImage)
    }

    private fun setImages(img: String?, imageView: ImageView) {
        val url =  context.getString(R.string.url) + "assets.admin_master/images/complaint/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }


    override fun getItemCount(): Int {
        return visitorObjects.size
    }


}