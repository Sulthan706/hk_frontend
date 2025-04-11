package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListDetailCountAbsentOprBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.detailAbsentProject.ListCountAbsentProjectModel

class ListDetailAbsentProjectAdapter(
    private val context: Context,
    var listAbsent: ArrayList<ListCountAbsentProjectModel>
): RecyclerView.Adapter<ListDetailAbsentProjectAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListDetailCountAbsentOprBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListDetailCountAbsentOprBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAbsent[position]

        holder.binding.tvTotalListDetailAbsentOpr.text = "${response.countAbsent}"
        holder.binding.tvStatusListDetailAbsentOpr.text = "${response.statusAbsent}"
    }

    override fun getItemCount(): Int {
        return listAbsent.size
    }
}