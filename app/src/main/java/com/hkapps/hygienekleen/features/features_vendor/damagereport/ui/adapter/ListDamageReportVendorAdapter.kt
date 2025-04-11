package com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListDamageVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorContent
import com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.activity.DetailDamageReportVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListDamageReportVendorAdapter(private var context: Context,
    var listBakVendor: ArrayList<ListBakVendorContent>):
RecyclerView.Adapter<ListDamageReportVendorAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemListDamageVendorBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListDamageVendorBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listBakVendor[position]

        //validate button disable
        when (item.validasiEmployee){
            "ACTIVE" -> {
                holder.binding.mbUploadFotoDamageReport.visibility = View.VISIBLE
                holder.binding.clCardDamageReportVendor.setOnClickListener{
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, false)
                    context.startActivity(Intent(context, DetailDamageReportVendorActivity::class.java))
                }
                holder.binding.mbUploadFotoDamageReport.setOnClickListener {
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, false)
                    context.startActivity(Intent(context, DetailDamageReportVendorActivity::class.java))
                }
            }
            "FINISHED" -> {
                holder.binding.mbFinishSubmit.visibility = View.VISIBLE
                holder.binding.clCardDamageReportVendor.setOnClickListener {
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, true)
                    context.startActivity(Intent(context, DetailDamageReportVendorActivity::class.java))
                }
                holder.binding.mbFinishSubmit.setOnClickListener {
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, true)
                    context.startActivity(Intent(context, DetailDamageReportVendorActivity::class.java))
                }
            }
            else -> {
                holder.binding.mbDisableUploadFotoDamageReport.visibility = View.VISIBLE
                holder.binding.mbDisableUploadFotoDamageReport.setOnClickListener {
                    Toast.makeText(context, "Menunggu persetujuan tim purchasing terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
                holder.binding.clCardDamageReportVendor.setOnClickListener {
                    Toast.makeText(context, "Menunggu persetujuan tim purchasing terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }


        holder.binding.tvBAKCodeNumber.text = if (item.kodeBak.isNullOrEmpty()){ "-" } else item.kodeBak
        holder.binding.tvDateBakTitle.text = if (item.tglDibuat.isNullOrEmpty()){ "-" } else item.tglDibuat
        holder.binding.tvBrandBak.text = ": " + if (item.merkMesin.isNullOrEmpty()){ "-" } else item.merkMesin
        holder.binding.tvTypeBak.text = ": " + if (item.jenisMesin.isNullOrEmpty()){ "-" } else item.jenisMesin




    }

    override fun getItemCount(): Int {
        return listBakVendor.size
    }

}
