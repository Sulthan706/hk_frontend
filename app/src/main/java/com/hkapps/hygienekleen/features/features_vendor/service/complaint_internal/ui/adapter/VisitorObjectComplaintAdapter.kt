package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemCtalkVisitorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.detailcomplaintinternal.VisitorObject
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity.CameraVisitorObjectActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class VisitorObjectComplaintAdapter(val activity: FragmentActivity, var listObject: ArrayList<VisitorObject>):
RecyclerView.Adapter<VisitorObjectComplaintAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemCtalkVisitorBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCtalkVisitorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listObject[position]
        val count = position + 1
        holder.binding.tvObjectName.text = "objek $count"
        holder.binding.tvDetailObjectName.text = item.namaObject.ifEmpty { "" }

        holder.binding.mVBeforeImageVisitor.setOnClickListener {
            activity.startActivity(Intent(activity, CameraVisitorObjectActivity::class.java))
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_OBJECT_VISITOR, item.idObject)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_IMG_OBJECT, "BEFORE")
        }
        holder.binding.mVAfterImageVisitor.setOnClickListener {
            activity.startActivity(Intent(activity, CameraVisitorObjectActivity::class.java))
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_OBJECT_VISITOR, item.idObject)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_IMG_OBJECT, "AFTER")
        }


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
        val url =  activity.getString(R.string.url) + "assets.admin_master/images/complaint/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                activity.resources.getIdentifier(uri, null, activity.packageName)
            val res = activity.resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(activity)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }



    override fun getItemCount(): Int {
        return listObject.size
    }


}