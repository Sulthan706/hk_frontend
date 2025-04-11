package com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListDacTeamClientBinding
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.Pekerjaan

class ListDacCalenderAdapter(
private val context: Context,
var listDacReport:ArrayList<Pekerjaan>
): RecyclerView.Adapter<ListDacCalenderAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ListDacTeamClientBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListDacTeamClientBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listDacReport[position]
        holder.binding.tvDacListTeamClient.text = response.activity
        holder.binding.tvDacTimeListTeamClient.text = "${response.startAt}-${response.endAt}"
    }

    override fun getItemCount(): Int {
        return listDacReport.size
    }
}