package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

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
import com.hkapps.hygienekleen.databinding.ItemCertificateVaccineBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.ContentVaccine
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ListCertificateVaksinActivity


class ListVaccineAdapter(
    private val context: Context,
    var listVaccine: ArrayList<ContentVaccine> ) :
    RecyclerView.Adapter<ListVaccineAdapter.ViewHolder>() {
    var selectedItem = -1
    private lateinit var listVaccineClick : ListVaccineClick
    inner class ViewHolder(val binding: ItemCertificateVaccineBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
            init {
                itemView.setOnClickListener(this)
            }

        override fun onClick(p0: View?) {
            val putVaccine = listVaccine[adapterPosition]
            listVaccineClick.onClickVaccine(
                putVaccine.vaccineCertificate,
                putVaccine.idVaccine,
                putVaccine.vaccineType
            )

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCertificateVaccineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = listVaccine[position]
        holder.binding.tvDosisType.text = items.vaccineType
        holder.binding.tvDateDosis.text = "Diupload pada : ${items.uploadDate}"

        val imgClient = items.vaccineCertificate
        val urlClient =
            context.getString(R.string.url) + "/assets.admin_master/images/vaccine/$imgClient"

        if (imgClient == "null" || imgClient == null || imgClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
//            holder.iv.setImageDrawable(res)
            holder.binding.ivCardVaccine.setImageDrawable(res)
        } else {
//            val radiusImage = context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.binding.ivCardVaccine)
        }
    }

    override fun getItemCount(): Int {
        return listVaccine.size
    }

    fun setListener(updateCertificateVaksinActivity: ListCertificateVaksinActivity) {
        this.listVaccineClick = updateCertificateVaksinActivity
    }


    interface ListVaccineClick {
        fun onClickVaccine(
            vaccineCertificate: String,
            idVaccine: Int,
            vaccineType: String
        )
    }


}


