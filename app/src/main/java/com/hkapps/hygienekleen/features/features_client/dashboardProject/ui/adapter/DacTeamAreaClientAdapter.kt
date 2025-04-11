package com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListDacTeamClientBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.ListPekerjaan

class DacTeamAreaClientAdapter(
    private val context: Context,
    var listDac: ArrayList<ListPekerjaan>
): RecyclerView.Adapter<DacTeamAreaClientAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListDacTeamClientBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListDacTeamClientBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listDac[position]
        holder.binding.tvDacListTeamClient.text = response.activity
        holder.binding.tvDacTimeListTeamClient.text = "${response.startAt} - ${response.endAt}"
    }

    override fun getItemCount(): Int {
        return listDac.size
    }
}