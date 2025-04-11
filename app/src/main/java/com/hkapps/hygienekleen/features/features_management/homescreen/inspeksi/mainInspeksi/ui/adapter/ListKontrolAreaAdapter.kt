package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListKontrolAreaInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea.Data

class ListKontrolAreaAdapter(
    private val context: Context,
    var listKontrolArea: ArrayList<Data>
): RecyclerView.Adapter<ListKontrolAreaAdapter.ViewHolder>() {

    private lateinit var listKontrolAreaCallBack: ListKontrolAreaCallBack

    inner class ViewHolder(val binding: ListKontrolAreaInspeksiBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

//        init {
//            itemView.setOnClickListener(this)
//        }

        override fun onClick(p0: View?) {
            val selected = listKontrolArea[adapterPosition]
//            listKontrolAreaCallBack.onClickItem(selected.idMeeting)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListKontrolAreaInspeksiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listKontrolArea[position]

        holder.binding.tvStatusItemListKontrolArea.text = when (response.typeVisit) {
            "KUNJUNGAN RUTIN" -> "Kunjungan Ruting"
            "SIDAK" -> "Sidak"
            else -> response.typeVisit
        }
        when (response.typeVisit) {
            "KUNJUNGAN RUTIN" -> holder.binding.tvStatusItemListKontrolArea.setTextColor(context.resources.getColor(R.color.blue2))
            "SIDAK" -> holder.binding.tvStatusItemListKontrolArea.setTextColor(context.resources.getColor(R.color.primary_color))
            else -> holder.binding.tvStatusItemListKontrolArea.setTextColor(context.resources.getColor(R.color.red2))
        }

        holder.binding.tvDateItemListKontrolArea.text = response.date
        holder.binding.tvCheckByItemListKontrolArea.text = response.adminMasterName
        holder.binding.tvTimeItemListKontrolArea.text = "-"

        if (response.manPowerCheck) {
            holder.binding.tvManpowerItemListKontrolArea.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            holder.binding.tvManpowerItemListKontrolArea.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }

        if (response.areaConditionCheck) {
            holder.binding.tvManpowerItemListKontrolArea.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_done_circle_green, 0, 0, 0)
        } else {
            holder.binding.tvManpowerItemListKontrolArea.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_chooser, 0, 0, 0)
        }
    }

    override fun getItemCount(): Int {
        return listKontrolArea.size
    }

    fun setListener(listKontrolAreaCallBack: ListKontrolAreaCallBack) {
        this.listKontrolAreaCallBack = listKontrolAreaCallBack
    }

    interface ListKontrolAreaCallBack {
//        fun onClickItem(idMeeting: Int)
    }
}