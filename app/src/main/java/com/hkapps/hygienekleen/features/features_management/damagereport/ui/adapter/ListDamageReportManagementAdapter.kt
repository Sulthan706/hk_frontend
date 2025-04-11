package com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListDamageReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport.ContentDamageReportManagement
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity.DetailDamageReportManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListDamageReportManagementAdapter(
    private var context: Context,
    var listDamageReport: ArrayList<ContentDamageReportManagement>
): RecyclerView.Adapter<ListDamageReportManagementAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListDamageReportManagementBinding):
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListDamageReportManagementAdapter.ViewHolder {
        return ViewHolder(
            ItemListDamageReportManagementBinding.inflate(
                LayoutInflater.from(parent.context),parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ListDamageReportManagementAdapter.ViewHolder,
        position: Int
    ) {
        val x = listDamageReport.sortedBy { it.validasiManagement == "DISABLED" }
        val item = x[position]
        //validate button disable
        when (item.validasiManagement){
            "ACTIVE" -> {
                holder.binding.mbUploadFotoDamageReport.visibility = View.VISIBLE
                holder.binding.clCardDamageReportManagement.setOnClickListener{
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, false)
                    context.startActivity(Intent(context, DetailDamageReportManagementActivity::class.java))
                }
                holder.binding.mbUploadFotoDamageReport.setOnClickListener {
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, false)
                    context.startActivity(Intent(context, DetailDamageReportManagementActivity::class.java))
                }
            }
            "FINISHED" -> {
                holder.binding.MbFinishUploadFotoDamageReport.visibility = View.VISIBLE
                holder.binding.clCardDamageReportManagement.setOnClickListener {
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, true)
                    context.startActivity(Intent(context, DetailDamageReportManagementActivity::class.java))
                }
                holder.binding.MbFinishUploadFotoDamageReport.setOnClickListener {
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, item.idDetailBakMesin)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, true)
                    context.startActivity(Intent(context, DetailDamageReportManagementActivity::class.java))
                }
            }
            else -> {
                holder.binding.MbDisableUploadFotoDamageReport.visibility = View.VISIBLE
                holder.binding.mbUploadFotoDamageReport.visibility = View.GONE
                holder.binding.MbDisableUploadFotoDamageReport.setOnClickListener {
                    Toast.makeText(context, "Menunggu persetujuan tim purchasing terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
                holder.binding.clCardDamageReportManagement.setOnClickListener {
                    Toast.makeText(context, "Menunggu persetujuan tim purchasing terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.binding.tvBAKCodeNumber.text = if (item.kodeBak.isNullOrEmpty()) { "-" } else item.kodeBak
        holder.binding.tvDateBakTitle.text = if (item.tglDibuat.isNullOrEmpty()) { "-" } else item.tglDibuat

        val textType = if (item.merkMesin.isNullOrEmpty()) { "-" } else item.merkMesin
        val textBrand = if (item.jenisMesin.isNullOrEmpty()) { "-" } else item.jenisMesin

        holder.binding.tvBrandBak.text = ": " + truncateText(textType, 18)
        holder.binding.tvTypeBak.text = ": " + truncateText(textBrand, 13)
        holder.binding.tvProjectBak.text = if (item.projectName.isNullOrEmpty()){ "-" } else item.projectName

    }

    override fun getItemCount(): Int {
        return listDamageReport.size
    }

    fun truncateText(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            "${text.substring(0, maxLength)}..."
        } else {
            text
        }
    }



}