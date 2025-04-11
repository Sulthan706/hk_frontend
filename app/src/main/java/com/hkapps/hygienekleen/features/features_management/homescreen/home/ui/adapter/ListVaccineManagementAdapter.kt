package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter

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
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listvaccinemanagement.ContentVaccineMgmnt
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt.ListVaccineManagementActivity

class ListVaccineManagementAdapter(
    private val context: Context,
    var listVaccineManagement: ArrayList<ContentVaccineMgmnt>
) : RecyclerView.Adapter<ListVaccineManagementAdapter.ViewHolder>() {
    var selectedItem = -1
    private lateinit var listVaccineMgntClick: ListVaccineMgmntClick

    inner class ViewHolder(val binding: ItemCertificateVaccineBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val putVaccineMgmnt = listVaccineManagement[adapterPosition]
            listVaccineMgntClick.onClickVaccineMgmnt(
                putVaccineMgmnt.vaccineCertificate,
                putVaccineMgmnt.idVaccine,
                putVaccineMgmnt.vaccineType
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCertificateVaccineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listVaccineManagement[position]
        holder.binding.tvDosisType.text = item.vaccineType
        holder.binding.tvDateDosis.text = "Diupload pada : ${item.uploadDate}"

        val imgClient = item.vaccineCertificate
        val urlClient =
            context.getString(R.string.url) + "assets.admin_master/images/vaccine/$imgClient"

        if (imgClient == "null" || imgClient == null || imgClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)


//            holder.iv.setImageDrawable(res)
            holder.binding.ivCardVaccine.setImageDrawable(res)
        } else {
            val radiusImage = context.resources.getDimensionPixelSize(R.dimen.rpb_default_corner_radius)
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
        return listVaccineManagement.size
    }

    fun setListener(listVaccineManagementActivity: ListVaccineManagementActivity) {
        this.listVaccineMgntClick = listVaccineManagementActivity
    }

    interface ListVaccineMgmntClick {
        fun onClickVaccineMgmnt(
            vaccineCertificate: String,
            idVaccine: Int,
            vaccineType: String)
    }

}


