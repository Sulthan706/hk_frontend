package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.SpinnerItemBinding

class SelectedChemicalsAdapter(
    var listSelectedChemicals: ArrayList<String>
): RecyclerView.Adapter<SelectedChemicalsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: SpinnerItemBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSpinnerItem.text = listSelectedChemicals[position]
    }

    override fun getItemCount(): Int {
        return listSelectedChemicals.size
    }

}