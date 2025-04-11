package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listArea.Data

class ListAreaInspeksiAdapter(
    var listArea: ArrayList<Data>,
    var selectedItem: Int = -1
): RecyclerView.Adapter<ListAreaInspeksiAdapter.ViewHolder>() {

    private lateinit var listAreaInspeksiCallBack: ListAreaInspeksiCallBack

    inner class ViewHolder(val binding: ListItemChooserBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val selected = listArea[adapterPosition]
            listAreaInspeksiCallBack.onAreaSelected(selected.idAreaList, selected.areaName)

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listArea[position]

        holder.binding.tvItemChooser.text = response.areaName
        holder.binding.ivDefaultChooser.visibility = View.VISIBLE
        holder.binding.ivSelectedChooser.visibility = View.GONE

        if (selectedItem == position) {
            holder.binding.ivDefaultChooser.visibility = View.INVISIBLE
            holder.binding.ivSelectedChooser.visibility = View.VISIBLE
        } else {
            holder.binding.ivDefaultChooser.visibility = View.VISIBLE
            holder.binding.ivSelectedChooser.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listArea.size
    }

    fun setListener(listAreaInspeksiCallBack: ListAreaInspeksiCallBack) {
        this.listAreaInspeksiCallBack = listAreaInspeksiCallBack
    }

    interface ListAreaInspeksiCallBack {
        fun onAreaSelected(areaId: Int, areaName: String)
    }
}