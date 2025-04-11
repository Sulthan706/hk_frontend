package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListVisitReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.Content
import java.text.SimpleDateFormat

class UnplannedVisitsManagementAdapter(
    var unplannedVisits: ArrayList<Content>
) : RecyclerView.Adapter<UnplannedVisitsManagementAdapter.ViewHolder>(){

    inner class ViewHolder (val binding: ItemListVisitReportManagementBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListVisitReportManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return unplannedVisits.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = unplannedVisits[position]

        // date formatted
        val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
        val dateBefore = sdfBefore.parse(response.date)
        val sdfAfter = SimpleDateFormat("dd MMM yyyy")
        val dateTxt = sdfAfter.format(dateBefore)

        // validate count visit by date
        if (position != 0) {
            if (response.date == unplannedVisits[position-1].date) {
                holder.binding.clDateListVisitReportManagement.visibility = View.GONE
            } else {
                val total = unplannedVisits.count { it.date == response.date}
                holder.binding.clDateListVisitReportManagement.visibility = View.VISIBLE
                holder.binding.tvDateListVisitReportManagement.text = dateTxt
                holder.binding.tvVisitsListVisitReportManagement.text = "$total visits"
            }
        } else {
            val total = unplannedVisits.count { it.date == response.date}
            holder.binding.clDateListVisitReportManagement.visibility = View.VISIBLE
            holder.binding.tvDateListVisitReportManagement.text = dateTxt
            holder.binding.tvVisitsListVisitReportManagement.text = "$total visits"
        }

        // set data visit
        holder.binding.tv3ListVisitProjectManagement.visibility = View.VISIBLE
        holder.binding.tvReasonListVisitProjectManagement.visibility = View.VISIBLE

        holder.binding.tvCheckInListVisitReportManagement.text = response.scanIn ?: "--:--"
        holder.binding.tvCheckOutListVisitReportManagement.text = response.scanOut ?: "--:--"
        holder.binding.tvProjectListVisitReportManagement.text = response.projectName
        holder.binding.tvProjectCodeListVisitReportManagement.text = response.projectCode
        holder.binding.tvReasonListVisitProjectManagement.text = response.divertedReason

    }

}