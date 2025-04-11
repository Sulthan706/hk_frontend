package com.hkapps.hygienekleen.features.features_management.service.resign.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemResignManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ContentResignManagement
import com.hkapps.hygienekleen.features.features_management.service.resign.ui.activity.ResignReasonAccActivity
import com.hkapps.hygienekleen.features.features_management.service.resign.ui.activity.ResignReasonDecActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListResignManagementAdapter(private val context: Context, var listResignManagement: ArrayList<ContentResignManagement>):
RecyclerView.Adapter<ListResignManagementAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemResignManagementBinding):
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemResignManagementBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listResignManagement[position]

        holder.binding.tvUserNameResign.text = if (item.userName.isNullOrEmpty()) "-" else item.userName
        holder.binding.tvNucAndPositionResign.text = ("${item.nucTurnOver} | ${item.jobCode}").ifEmpty { "" }
        holder.binding.tvUserProject.text = if (item.projectCode.isNullOrEmpty()) "-" else item.projectCode
        holder.binding.tvUserRequestResignDate.text = if (item.tanggalPermintaan.isNullOrEmpty()) "-" else item.tanggalPermintaan
        holder.binding.tvCreateResignVendor.text = if (item.createdAtTurnOver.isNullOrEmpty()) "-" else item.createdAtTurnOver
        holder.binding.tvUserReasonResignResult.text = if (item.reason.isNullOrEmpty()) "-" else item.reason

        when(item.status){
            "Disetujui" -> {
                holder.binding.btnApprovedResign.visibility = View.VISIBLE
                holder.binding.btnDeclinedResign.visibility = View.GONE
                holder.binding.btnApproveResign.visibility = View.GONE
                holder.binding.btnDeclineResign.visibility = View.GONE
                holder.binding.btnEskalasiHcResign.visibility = View.GONE
                holder.binding.tvTitleReasonResignResult.visibility = View.VISIBLE
                holder.binding.tvUserReasonResignResult.visibility = View.VISIBLE
            }
            "Menunggu Approval" -> {
                holder.binding.btnApprovedResign.visibility = View.GONE
                holder.binding.btnDeclinedResign.visibility = View.GONE
                holder.binding.btnApproveResign.visibility = View.VISIBLE
                holder.binding.btnDeclineResign.visibility = View.VISIBLE
                holder.binding.btnEskalasiHcResign.visibility = View.GONE
            }
            "Ditolak" -> {
                holder.binding.btnApprovedResign.visibility = View.GONE
                holder.binding.btnApproveResign.visibility = View.GONE
                holder.binding.btnDeclinedResign.visibility = View.VISIBLE
                holder.binding.btnDeclineResign.visibility = View.GONE
                holder.binding.btnEskalasiHcResign.visibility = View.GONE
                holder.binding.tvTitleReasonResignResult.visibility = View.VISIBLE
                holder.binding.tvUserReasonResignResult.visibility = View.VISIBLE
            }
            "Escalated to HC" -> {
                holder.binding.btnApprovedResign.visibility = View.GONE
                holder.binding.btnApproveResign.visibility = View.GONE
                holder.binding.btnDeclinedResign.visibility = View.GONE
                holder.binding.btnDeclineResign.visibility = View.GONE
                holder.binding.btnEskalasiHcResign.visibility = View.VISIBLE
            }
        }
        //btn
        holder.binding.btnApproveResign.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_NAME, item.userName)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_NUC, item.nucTurnOver)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_POSITION, item.jobCode)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_TURN_OVER, item.idTurnOver)
            context.startActivity(Intent(context, ResignReasonAccActivity::class.java))
        }
        holder.binding.btnDeclineResign.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_NAME, item.userName)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_NUC, item.nucTurnOver)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_POSITION, item.jobCode)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_TURN_OVER, item.idTurnOver)
            context.startActivity(Intent(context, ResignReasonDecActivity::class.java))
        }

        loadImage(item.userPhotoProfile, holder.binding.ivUserProfileResign)


    }

    override fun getItemCount(): Int {
        return listResignManagement.size
    }

    private fun loadImage(img: String?, imageView: ImageView) {
        // Show the progress bar for the current image while loading

        if (img == "null" || img == null || img == "") {
            val uri = "@drawable/ic_camera_black" // Replace with your default image
            val imageResource = context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imageResource)
            imageView.setImageDrawable(res)
            // Hide the progress bar when the default image is set
        } else {
            val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
//            val url = getString(R.string.url) + "rkb/$img"

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Because file name is always the same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Handle image loading failure here (e.g., show an error message)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide the progress bar for the current image when it is loaded successfully
                        return false
                    }
                })
                .into(imageView)
        }
    }



}