package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListLateReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.Data
import java.text.SimpleDateFormat

class ListLateReportAdapter (var listLateReport: ArrayList<Data>) :
    RecyclerView.Adapter<ListLateReportAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListLateReportBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListLateReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listLateReport.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLateReport[position]

        // change date format
        val dateString = item.date
        val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
        val dateParamBefore = sdfBefore.parse(dateString)
        val sdfAfter = SimpleDateFormat("dd MMM yyyy")
        val date = sdfAfter.format(dateParamBefore)

        holder.binding.tvDateListLateReport.text = date
        holder.binding.tvCheckInListLateReport.text = item.absenMasuk
        holder.binding.tvLateListLateReport.text = item.keterlambatan
    }

}